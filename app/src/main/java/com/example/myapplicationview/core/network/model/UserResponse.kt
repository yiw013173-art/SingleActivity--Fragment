package com.example.myapplicationview.core.network.model

/**
 * 对应接口返回的最外层大对象
 */
data class UserResponse(
    val results: List<UserDto> = emptyList(),
    val info: InfoDto = InfoDto()
)

/**
 * 用户核心数据对象
 */
data class UserDto(
    val gender: String,
    val name: NameDto,
    val location: LocationDto,
    val email: String,
    val login: LoginDto,
    val dob: DateOfBirthDto,
    val registered: RegisteredDto,
    val phone: String,
    val cell: String,
    val id: IdDto,
    val picture: PictureDto,
    val nat: String
)

data class NameDto(
    val title: String,
    val first: String,
    val last: String
)

data class LocationDto(
    val street: StreetDto,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String, // 注意：邮编建议用 String，防止 0 开头的丢失
    val coordinates: CoordinatesDto,
    val timezone: TimezoneDto
)

data class StreetDto(
    val number: Int,
    val name: String
)

data class CoordinatesDto(
    val latitude: String,
    val longitude: String
)

data class TimezoneDto(
    val offset: String,
    val description: String
)

data class LoginDto(
    val uuid: String,
    val username: String,
    // 如果不需要敏感信息，可以删减字段
    val md5: String,
    val sha1: String,
    val sha256: String
)

data class DateOfBirthDto(
    val date: String,
    val age: Int
)

data class RegisteredDto(
    val date: String,
    val age: Int
)

data class IdDto(
    val name: String?,
    val value: String?
)

data class PictureDto(
    val large: String,
    val medium: String,
    val thumbnail: String
)

data class InfoDto(
    val seed: String = "",
    val results: Int = 0,
    val page: Int = 0,
    val version: String = ""
)