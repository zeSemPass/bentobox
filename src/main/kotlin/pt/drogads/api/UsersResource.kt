package pt.drogads.api

import io.quarkus.security.identity.SecurityIdentity
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.resteasy.reactive.NoCache
import pt.drogads.domain.entity.User
import java.net.URI
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UsersResource {

    @Inject
    var identity: SecurityIdentity? = null

    @GET
    @Path("/me")
    @NoCache
    fun me(): SecurityIdentity? {
        return identity
    }


    @POST
    @Operation(summary = "Creates a User", description = "Creates a new user (enabled) with default 'USER' role")
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "201", description = "User Created"
            ),
            APIResponse(
                responseCode = "409", description = "Conflict: User already exists"
            ),
            APIResponse(
                responseCode = "400", description = "Bad request"
            )]
    )
    fun create(@Valid user: User): Uni<Response> {
        return User.findByUsername(user.username).flatMap { u ->
            return@flatMap if (u !== null) {
                Uni.createFrom().item(Response.status(Response.Status.CONFLICT).build())
            } else
                user.persist<User>().onItem()
                    .transform { r -> Response.created(URI.create("/api/users/" + r.id.toString())).build() }
        }

    }

    @GET
    @RolesAllowed("admin")
    @Operation(summary = "Gets all users", description = "Lists all available users")
    fun read() = User.streamAll()

    @PATCH
    @RolesAllowed("Admin")
    @Operation(summary = "Updates a user", description = "Lists all available users")
    fun update(@Valid user: User) = User.update(user)

    @DELETE
    @RolesAllowed("admin")
    @Operation(
        summary = "Disables a User",
        description = "Performs a soft delete of the user by setting the 'active' flag to false"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "204", description = "User (soft)Deleted"
            )
        ]
    )
    fun delete(username: String) = User.softDeleteUser(username)
}