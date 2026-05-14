package com.kaushalya.kaushalyakarnataka.model

enum class Role { WORKER, CUSTOMER }
enum class PriceType { FIXED, STARTING }
enum class RequestStatus { PENDING, CONTACTED }
enum class Tab { HOME, SERVICES, REVIEWS, PROFILE, REQUESTS }

data class Service(

    val id: String = "",

    val name: String = "",

    val description: String = "",

    val price: Int = 0,

    val priceType: PriceType = PriceType.FIXED
)
data class Review(

    val id: String = "",

    val authorName: String = "",

    val rating: Int = 0,

    val text: String = "",

    val date: String = ""
)

data class HireRequest(

    val id: String = "",

    val workerId: String = "",

    val customerName: String = "",

    val customerPhone: String = "",

    val requestText: String = "",

    val preferredTime: String = "",

    val status: RequestStatus = RequestStatus.PENDING,

    val timestamp: String = ""
)

data class WorkerProfile(

    val id: String = "",

    val name: String = "",

    val category: String = "",

    val experience: Int = 0,

    val rating: Double = 0.0,

    val reviewCount: Int = 0,

    val startingPrice: Int = 0,

    val phone: String = "",

    val bio: String = "",

    val avatar: String = "",

    val portfolio: List<String> = emptyList(),

    val services: List<Service> = emptyList(),

    val reviews: List<Review> = emptyList()
)

data class WorkerService(

    val id: String = "",

    val workerId: String = "",

    val name: String = "",

    val description: String = "",

    val price: Int = 0,

    val priceType: PriceType = PriceType.FIXED
)

data class WorkerReview(

    val id: String = "",

    val workerId: String = "",

    val authorName: String = "",

    val rating: Int = 0,

    val text: String = "",

    val date: String = ""
)

data class User(

    val id: String = "",

    val name: String = "",

    val role: Role = Role.CUSTOMER,

    val phone: String = "",

    val category: String? = null
)