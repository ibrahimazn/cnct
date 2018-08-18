-- Dump completed on 2018-02-12 15:53:31
INSERT INTO `department`(`id`,`description`,`is_active`,`name`)  SELECT  1, 'Development', 1, 'Development'  FROM DUAL  WHERE NOT EXISTS (SELECT id FROM `department` WHERE id=1)  LIMIT 1;

INSERT INTO `user` (`user_id`,`active`,`department_id`,`email`,`last_name`,`first_name`,`password`,`primary_phone`,`user_name`,`user_type`,`profile_img_file`,`profile_img_path`) SELECT 1,1,1,'slmlp@assistanz.com','assistanz','slmlp','IFWUiDPUg+aSdsaW0m+nTw==','9047904523','slmlp@assistanz.com',0,NULL,NULL FROM DUAL  WHERE NOT EXISTS (SELECT user_id FROM `user` WHERE user_id=1)  LIMIT 1 ;

INSERT INTO `role` (`role_id`,`created_user_id`,`created_date_time`,`is_active`,`role`,`status`,`updated_user_id`,`updated_date_time`,`version`) SELECT 1,NULL,NULL,1,'ADMIN','ENABLED',NULL,NULL,7 FROM DUAL WHERE NOT EXISTS (SELECT role_id FROM `role` WHERE role_id=1)  LIMIT 1;

INSERT INTO `groups` (`id`,`name`,`created_user_id`,`created_date_time`,`is_active`,`updated_user_id`,`updated_date_time`,`version`) SELECT 1,'Department',NULL,NULL,1,NULL,NULL,NULL FROM DUAL WHERE NOT EXISTS (SELECT id FROM `groups` WHERE id=1)  LIMIT 1;


INSERT INTO `permission` (`permission_id`,`created_by`,`created_date_time`,`group_id`,`is_active`,`permission`,`status`,`updated_by`,`updated_date_time`,`version`) SELECT 1,NULL,NULL,1,1,'DEPARTMENT_LIST',0,NULL,NULL,NULL FROM DUAL WHERE NOT EXISTS (SELECT permission_id FROM `permission` WHERE permission_id=1)  LIMIT 1;
INSERT INTO `permission` (`permission_id`,`created_by`,`created_date_time`,`group_id`,`is_active`,`permission`,`status`,`updated_by`,`updated_date_time`,`version`) SELECT 2,NULL,NULL,1,1,'DEPARTMENT_ADD',0,NULL,NULL,NULL FROM DUAL WHERE NOT EXISTS (SELECT permission_id FROM `permission` WHERE permission_id=2)  LIMIT 1;


INSERT INTO `user_role` (`user_id`,`role_id`) SELECT 1,1 FROM DUAL WHERE NOT EXISTS (SELECT user_id FROM `user_role` WHERE user_id=1 AND role_id=1)  LIMIT 1;

INSERT INTO `role_permission` (`role_id`,`permission_id`) SELECT 1,1 FROM DUAL WHERE NOT EXISTS (SELECT role_id FROM `role_permission` WHERE role_id=1 AND permission_id=1) LIMIT 1;

INSERT INTO `namespaces` (`id`,`name`,`created_user_id`,`is_active`) SELECT 1,'slmlp-assistanz-com',1,1 FROM DUAL WHERE NOT EXISTS (SELECT id FROM `namespaces` WHERE id=1) LIMIT 1;

INSERT INTO `launcher` (`id`,`description`,`image`,`image_name`,`internal_port`,`is_active`,`name`,`platform_tool`,`type`)
SELECT 1, 'Rstudio','10.160.0.2:31757/assistanz/r-rstudio-libraries:v1','rstudio','8787',1,'Rstudio','Rstudio','Launchers'
FROM DUAL WHERE NOT EXISTS (SELECT id FROM `launcher` WHERE id=1) LIMIT 1;

INSERT INTO `launcher` (`id`,`description`,`image`,`image_name`,`internal_port`,`is_active`,`name`,`platform_tool`,`type`)
SELECT 2, 'Centos Jupyter','10.160.0.2:31757/assistanz/centos-jupyter:latest','centos-jupyter','8888',1,'centos-jupyter','Centos Jupyter','Launchers'
FROM DUAL WHERE NOT EXISTS (SELECT id FROM `launcher` WHERE id=2) LIMIT 1;

INSERT INTO `launcher` (`id`,`description`,`image`,`image_name`,`internal_port`,`is_active`,`name`,`platform_tool`,`type`)
SELECT 3, 'OpenFaas Wrapper','10.160.0.2:31757/assistanz/openfas-base-v1:latest','Openfaas Python Centos','8787',1,'openfas-base-v1','OpenFaas Wrapper','MODEL_PREDICTION'
FROM DUAL WHERE NOT EXISTS (SELECT id FROM `launcher` WHERE id=3) LIMIT 1;

INSERT INTO `launcher` (`id`,`description`,`image`,`image_name`,`internal_port`,`is_active`,`name`,`platform_tool`,`type`)
SELECT 4, 'OpenFaas Rstudio','10.160.0.2:31757/assistanz/openfas-r-base:v1','Openfaas Rstudio','8787',1,'openfas-r-base:v1','OpenFaas Wrapper','MODEL_PREDICTION'
FROM DUAL WHERE NOT EXISTS (SELECT id FROM `launcher` WHERE id=4) LIMIT 1;

INSERT INTO `training_engine` (`id`, `description`, `image`,`image_name`,`internal_port`, `is_active`, `name`, `platform_tool`,`type`) SELECT 2,'Custom python image','10.160.0.2:31757/assistanz/openfas-base:latest','custom-python-model','8080',1,'custom-model','Python','CustomEngine' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `training_engine` WHERE id=2) LIMIT 1;
INSERT INTO `training_engine` (`id`, `description`, `image`,`image_name`,`internal_port`, `is_active`, `name`, `platform_tool`,`type`) SELECT 3,'TensorFlow','10.160.0.2:31757/assistanz/openfaas-python-base:v1','tensor-flow','6006',1,'tensor-flow','TensorFlow','TrainingEngine' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `training_engine` WHERE id=3) LIMIT 1;
INSERT INTO `training_engine` (`id`, `description`, `image`,`image_name`,`internal_port`, `is_active`, `name`, `platform_tool`,`type`) SELECT 1,'Custom R image','10.160.0.2:31757/assistanz/openfas-r-base:v1','custom-R-model','8080',1,'custom-R-model','R','CustomEngine' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `training_engine` WHERE id=1) LIMIT 1;


INSERT INTO `slmlp`.`platform_tools` (`id`, `is_active`, `name`) SELECT 1, 1, 'CENTOS + PYTHON' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `platform_tools` WHERE id=1) LIMIT 1;

INSERT INTO `slmlp`.`dataset_storage_type` (`id`, `is_active`, `name`) SELECT 1, 1, 'GIT' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `dataset_storage_type` WHERE id=1) LIMIT 1;
INSERT INTO `slmlp`.`dataset_storage_type` (`id`, `is_active`, `name`) SELECT 2, 1, 'Amazon S3' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `dataset_storage_type` WHERE id=2) LIMIT 1;
INSERT INTO `slmlp`.`dataset_storage_type` (`id`, `is_active`, `name`) SELECT 3, 1, 'Tera Data' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `dataset_storage_type` WHERE id=3) LIMIT 1;

INSERT INTO `slmlp`.`datasource` (`id`, `is_active`, `name`) SELECT 1, 1, 'GIT' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `datasource` WHERE id=1) LIMIT 1;
INSERT INTO `slmlp`.`datasource` (`id`, `is_active`, `name`) SELECT 2, 1, 'S3' FROM DUAL WHERE NOT EXISTS (SELECT id FROM `datasource` WHERE id=2) LIMIT 1;

/**  Modification 14-03-2018*/
ALTER TABLE user MODIFY email varchar(255) null;
ALTER TABLE user MODIFY primary_phone varchar(255) null;
ALTER TABLE scoring MODIFY score_result TEXT null;
