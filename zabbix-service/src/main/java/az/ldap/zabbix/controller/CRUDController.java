package az.ldap.zabbix.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generic Controller to handle REST and common actions for Application.
 *
 * @param <T>
 */
public class CRUDController<T> extends ExceptionHandlingController {

    /**
     * Rest action to create an entity from json object.
     *
     * @param t - The generic entity to create
     * @return the entity in json based on the Accept headers
     * @throws Exception in case of any errors
     */
    @RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public T create(@RequestBody T t) throws Exception {
        return createT(t);
    }

    /**
     * Rest action to update an entity from json object.
     *
     * @param id - Id of the entity to update
     * @param t - The generic entity to update
     * @return the entity in json based on the Accept headers
     * @throws Exception if internal error occurs.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {
            MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public T update(@RequestBody T t, @PathVariable("id") String id) throws Exception {
        return updateT(id, t);
    }

    /**
     * Rest action to delete Entity.
     *
     * @param id - of the entity
     * @throws Exception if internal error occurs.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) throws Exception {
        deleteT(id);
    }

    /**
     * Rest action to get Entity.
     *
     * @param id of the entity
     * @return the entity T
     * @throws Exception if internal error occurs.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public T read(@PathVariable("id") String id) throws Exception {
        return get(id);
    }

    /**
     * Rest action to get list of Entities.
     *
     * @param sortBy example request param: sort(+name) - for Ascending order sort(-name) - for Descending order
     * @param range example request header: Range: items=0-9
     * @param limit example request param: 25
     * @param request to set range
     * @param response to set range
     * @return list example response header: Content-Range: items 0-9/4500
     * @throws Exception if any issue occurs
     */
    @RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<T> list(@RequestParam(required = false) String sortBy, @RequestHeader(value = "Range") String range,
            @RequestParam(required = false) Integer limit, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        return null;
    }

    /**
     * Protected create method.
     *
     * @param t type to set
     * @return entity type
     * @throws Exception if any issue occurs
     */
    protected T createT(T t) throws Exception {
        return t;
    }

    /**
     * Protected updated method.
     *
     * @param id of the entity
     * @param t type to set update
     * @return entity type
     * @throws Exception if any issue occurs
     */
    protected T updateT(String id, T t) throws Exception {
        return t;
    }

    /**
     * Protected delete method.
     *
     * @param id of the entity to delete
     * @throws Exception if any issue occurs
     */
    protected void deleteT(String id) throws Exception {
        // Do nothing
    }

    /**
     * Protected get method.
     *
     * @param id of the entity to get
     * @return entity type
     * @throws Exception if any issue occurs
     */
    protected T get(String id) throws Exception {
        return null;
    }

    /**
     * Protected get method.
     *
     * @return entity type
     * @throws Exception if any issue occurs
     */
    protected List<T> getAll() throws Exception {
        return null;
    }
}
