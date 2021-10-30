package pt.drogads.api

import pt.drogads.domain.User
import javax.validation.Valid
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/users")
class UsersResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    fun register(@Valid user: User): Response {
        val result = user.persist<User>()
        return Response.status(Response.Status.CREATED).entity("created $result").build()
    }



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun list() = User("test", "123123", "test@test.com").toString()
}