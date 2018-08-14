package az.ldap.sync.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Event response from RabbitMQ.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseEvent {
    /** event id. */
    private String id;

    /** event uuid. */
    private String entityuuid;

    /** event status. */
    private String status;

    /** event date and time. */
    private String eventDateTime;

    /** event domain account. */
    private String account;

    /** event user. */
    private String user;

    /** event type. */
    private String event;

    /** event type starts with. */
    private String eventStart;

    /** event type. */
    private String description;

    /** event subject. */
    private String subject;

    /** event body. */
    private String body;

    /** VM for event. */
    private String VirtualMachine;

    /** Zone for event. */
    private String DataCenter;

    /** Zone for event. */
    private String zone;

    /** Network for event. */
    private String Network;

    /** Resource for event. */
    private String resource;

    /** ServiceOffering for event. */
    private String ServiceOffering;

    /** ServiceOffering for event. */
    private String VirtualMachineTemplate;

    /** entity for event. */
    private String entity;

    /**
     * Get entity uuid.
     *
     * @return the entityuuid.
     */
    public String getEntityuuid() {
        return entityuuid;
    }

    /**
     * Set entity uuid.
     *
     * @param entityuuid the entity uuid to set.
     */
    public void setEntityuuid(String entityuuid) {
        this.entityuuid = entityuuid;
    }

    /**
     * Get status.
     *
     * @return the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set status.
     *
     * @param status the status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get event date and time.
     *
     * @return the eventDateTime.
     */
    public String getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Set event data and time.
     *
     * @param eventDateTime the event date and time to set
     */
    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    /**
     * Get account uuid.
     *
     * @return the account.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Set account uuid.
     *
     * @param account the account to set.
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Get user uuid.
     *
     * @return the user.
     */
    public String getUser() {
        return user;
    }

    /**
     * Set user uuid.
     *
     * @param user the user to set.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get event type.
     *
     * @return the event.
     */
    public String getEvent() {
        return event;
    }

    /**
     * Set event type.
     *
     * @param event the event to set.
     */
    public void setEvent(String event) {
        this.event = event;
        setEventStart(this.event);
    }

    /**
     * Get event description.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set event description.
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get event subject.
     *
     * @return the subject.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set event subject.
     *
     * @param subject the subject to set.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get event body.
     *
     * @return the body.
     */
    public String getBody() {
        return body;
    }

    /**
     * Set event body.
     *
     * @param body the body to set.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Get event id.
     *
     * @return the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Set event id.
     *
     * @param id the id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get zone-datacenter uuid.
     *
     * @return the dataCenter.
     */
    public String getDataCenter() {
        return DataCenter;
    }

    /**
     * Set zone-datacenter uuid.
     *
     * @param dataCenter the dataCenter to set.
     */
    public void setDataCenter(String dataCenter) {
        DataCenter = dataCenter;
    }

    /**
     * Get zone uuid.
     *
     * @return the zone.
     */
    public String getZone() {
        return zone;
    }

    /**
     * Set zone uuid.
     *
     * @param zone the zone to set.
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * Get network uuid.
     *
     * @return the network.
     */
    public String getNetwork() {
        return Network;
    }

    /**
     * Set network uuid.
     *
     * @param network the network to set.
     */
    public void setNetwork(String network) {
        Network = network;
    }

    /**
     * Get resource name.
     *
     * @return the resource.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Set resource name.
     *
     * @param resource the resource to set.
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * Get VM uuid.
     *
     * @return the virtualMachine.
     */
    public String getVirtualMachine() {
        return VirtualMachine;
    }

    /**
     * Set VM uuid.
     *
     * @param virtualMachine the virtual machine to set.
     */
    public void setVirtualMachine(String virtualMachine) {
        VirtualMachine = virtualMachine;
    }

    /**
     * Get service offering uuid.
     *
     * @return the serviceOffering.
     */
    public String getServiceOffering() {
        return ServiceOffering;
    }

    /**
     * Set service offering uuid.
     *
     * @param serviceOffering the service offering to set.
     */
    public void setServiceOffering(String serviceOffering) {
        ServiceOffering = serviceOffering;
    }

    /**
     * Get template uuid.
     *
     * @return the virtualMachineTemplate.
     */
    public String getVirtualMachineTemplate() {
        return VirtualMachineTemplate;
    }

    /**
     * Set template uuid.
     *
     * @param virtualMachineTemplate the virtual machine template to set.
     */
    public void setVirtualMachineTemplate(String virtualMachineTemplate) {
        VirtualMachineTemplate = virtualMachineTemplate;
    }

    /**
     * Get event entity.
     *
     * @return the entity.
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Set event entity.
     *
     * @param entity the entity to set.
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * Get event start substring.
     *
     * @return the event start with string of event type.
     */
    public String getEventStart() {
        return eventStart;
    }

    /**
     * Set event start substring.
     *
     * @param eventStart the event start with string to set.
     */
    public void setEventStart(String eventStart) {
        if (this.event != null) {
            this.eventStart = this.event.substring(0, this.event.indexOf('.', 0)) + ".";
        } else {
            this.eventStart = eventStart;
        }
    }

    @Override
    public String toString() {
        return "ResponseEvent [id=" + id + ", entityuuid=" + entityuuid + ", status=" + status + ", eventDateTime="
                + eventDateTime + ", account=" + account + ", user=" + user + ", event=" + event + ", eventStart = "
                + eventStart + " description=" + description + ", subject=" + subject + ", body=" + body
                + ", VirtualMachine=" + VirtualMachine + ", DataCenter=" + DataCenter + ", zone=" + zone + ", Network="
                + Network + ", resource=" + resource + ", ServiceOffering=" + ServiceOffering
                + ", VirtualMachineTemplate=" + VirtualMachineTemplate + ", entity=" + entity + "]";
    }
}
