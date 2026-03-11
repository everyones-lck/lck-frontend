package every.lol.com.core.domain

sealed class DomainException(
    override val message: String? = null
) : Throwable(message) {
    data class BadRequestException(override val message: String) : DomainException()
    data class InvalidJwtTokenException(override val message: String) : DomainException()
    data class NoPermissionException(override val message: String) : DomainException()
    data class NotFoundException(override val message: String) : DomainException()
    data class ServerErrorException(override val message: String) : DomainException()
    data class BadGatewayException(override val message: String) : DomainException()
    data class NetworkException(override val message: String) : DomainException()
    data class UnknownException(override val message: String) : DomainException()
}