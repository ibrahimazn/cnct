package az.ldap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import az.ldap.zabbix.entity.DefaultItems;
import az.ldap.zabbix.entity.DefaultTriggers;

@EnableBatchProcessing
@Configuration
public class DataLoad {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean(name = "itemLoad")
	public Job loadItem() {
		return jobBuilderFactory.get("readItemFile").incrementer(new RunIdIncrementer()).start(stepWindows()).next(stepLinux()).build();
	}

	@Bean(name = "triggerLoad")
	public Job loadTrigger() {
		return jobBuilderFactory.get("readTriggerFile").incrementer(new RunIdIncrementer()).start(step1()).next(step2()).build();
	}

	@Bean
	public Step stepWindows() {
		return stepBuilderFactory.get("step1").<DefaultItems, DefaultItems>chunk(10).reader(readerWindows()).writer(writer())
				.build();
	}
	
	@Bean
	public Step stepLinux() {
		return stepBuilderFactory.get("step2").<DefaultItems, DefaultItems>chunk(10).reader(readerLinux()).writer(writer())
				.build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step3").<DefaultTriggers, DefaultTriggers>chunk(10).reader(readerWindowsTriggers()).writer(writerTrigger())
				.build();
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step4").<DefaultTriggers, DefaultTriggers>chunk(10).reader(readerLinuxTriggers()).writer(writerTrigger())
				.build();
	}

	@Bean
	public FlatFileItemReader<DefaultItems> readerWindows() {
		FlatFileItemReader<DefaultItems> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("windows-metrics.csv"));
		reader.setLineMapper(new DefaultLineMapper<DefaultItems>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] {"id", "osType", "category", "name", "key_", "type", "valueType", "dataType",
								"graph", "units", "isDefault", "delay", "usage", "triggerStatus"
								});
						setDelimiter("-");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<DefaultItems>() {
					{
						setTargetType(DefaultItems.class);
					}
				});
			}
		});
		return reader;
	}
	
	@Bean
	public FlatFileItemReader<DefaultItems> readerLinux() {
		FlatFileItemReader<DefaultItems> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("linux-metrics.csv"));
		reader.setLineMapper(new DefaultLineMapper<DefaultItems>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] {"id", "osType", "category", "name", "key_", "type", "valueType", "dataType",
								"graph", "units", "isDefault", "delay", "usage", "triggerStatus"
								});
						setDelimiter("-");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<DefaultItems>() {
					{
						setTargetType(DefaultItems.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public MongoItemWriter<DefaultItems> writer() {
		MongoItemWriter<DefaultItems> writer = new MongoItemWriter<DefaultItems>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection("default_items");
		return writer;
	}
	
	@Bean
	public FlatFileItemReader<DefaultTriggers> readerWindowsTriggers() {
		FlatFileItemReader<DefaultTriggers> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("windows-triggers.csv"));
		reader.setLineMapper(new DefaultLineMapper<DefaultTriggers>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] {"id", "osType", "category", "description", "expression", "units", "message"});
						setDelimiter("--");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<DefaultTriggers>() {
					{
						setTargetType(DefaultTriggers.class);
					}
				});
			}
		});
		return reader;
	}
	
	@Bean
	public FlatFileItemReader<DefaultTriggers> readerLinuxTriggers() {
		FlatFileItemReader<DefaultTriggers> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("linux-triggers.csv"));
		reader.setLineMapper(new DefaultLineMapper<DefaultTriggers>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] {"id", "osType", "category", "description", "expression", "units", "message"});
						setDelimiter("--");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<DefaultTriggers>() {
					{
						setTargetType(DefaultTriggers.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public MongoItemWriter<DefaultTriggers> writerTrigger() {
		MongoItemWriter<DefaultTriggers> writer = new MongoItemWriter<DefaultTriggers>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection("default_triggers");
		return writer;
	}
}
