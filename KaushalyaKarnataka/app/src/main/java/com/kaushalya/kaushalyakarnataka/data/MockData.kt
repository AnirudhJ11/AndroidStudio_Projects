package com.kaushalya.kaushalyakarnataka.data

import com.kaushalya.kaushalyakarnataka.model.*

object MockData {

    val categories = listOf(
        "All",
        "Electrician",
        "Plumber",
        "Carpenter",
        "Painter",
        "Mason",
        "Others"
    )

    val initialWorkers = listOf(

        WorkerProfile(
            id = "w1",
            name = "Ramesh Kumar",
            category = "Electrician",
            experience = 8,
            rating = 4.8,
            reviewCount = 24,
            startingPrice = 200,
            phone = "9876543210",
            bio = "Expert in residential electrical fittings, fan repairs, and inverter setups. Always on time and guarantees quality work.",
            avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop",

            portfolio = listOf(
                "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=400&h=300&fit=crop",
                "https://images.unsplash.com/photo-1540104539509-7a31c51dcdea?w=400&h=300&fit=crop"
            ),

            services = listOf(

                Service(
                    id = "s1",
                    name = "Fan Repair",
                    description = "Fixing ceiling or table fan motor and capacitor issues.",
                    price = 200,
                    priceType = PriceType.FIXED
                ),

                Service(
                    id = "s2",
                    name = "Switchboard Fix",
                    description = "Replacing modules, switches, or entire boards.",
                    price = 350,
                    priceType = PriceType.STARTING
                )
            ),

            reviews = listOf(

                Review(
                    id = "r1",
                    authorName = "Sanjay M.",
                    rating = 5,
                    text = "Very prompt and professional.",
                    date = "2 days ago"
                ),

                Review(
                    id = "r2",
                    authorName = "Kavya S.",
                    rating = 4,
                    text = "Fixed the fan nicely, but arrived 10 minutes late.",
                    date = "1 week ago"
                )
            )
        ),

        WorkerProfile(
            id = "w2",
            name = "Asha Patil",
            category = "Plumber",
            experience = 5,
            rating = 4.5,
            reviewCount = 15,
            startingPrice = 150,
            phone = "9123456780",
            bio = "Specialist in resolving pipeline leaks, sensor taps, and complete bathroom remodeling.",
            avatar = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150&h=150&fit=crop",

            portfolio = listOf(
                "https://images.unsplash.com/photo-1585704032915-c3400ca199e7?w=400&h=300&fit=crop"
            ),

            services = listOf(

                Service(
                    id = "s3",
                    name = "Tap Leak Fix",
                    description = "Fix dripping taps or pipe joints.",
                    price = 150,
                    priceType = PriceType.FIXED
                ),

                Service(
                    id = "s4",
                    name = "Bathroom Fitting",
                    description = "Complete geyser or shower unit installation.",
                    price = 800,
                    priceType = PriceType.STARTING
                )
            ),

            reviews = listOf(

                Review(
                    id = "r3",
                    authorName = "Rajesh K.",
                    rating = 5,
                    text = "Asha arrived quickly and sorted the leak in no time.",
                    date = "3 days ago"
                )
            )
        ),

        WorkerProfile(
            id = "w3",
            name = "Karthik Gowda",
            category = "Carpenter",
            experience = 12,
            rating = 4.2,
            reviewCount = 42,
            startingPrice = 500,
            phone = "9988776655",
            bio = "Premium woodwork, furniture repair, and customized modular kitchen designs.",
            avatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&h=150&fit=crop",

            portfolio = emptyList(),

            services = listOf(

                Service(
                    id = "s5",
                    name = "Chair Repair",
                    description = "Fixing wobbling or broken wooden chairs.",
                    price = 500,
                    priceType = PriceType.FIXED
                )
            ),

            reviews = emptyList()
        )
    )

    val initialRequests = listOf(

        HireRequest(
            id = "req1",
            workerId = "w1",
            customerName = "Rahul Dravid",
            customerPhone = "9000000001",
            requestText = "Need living room fan repaired ASAP.",
            preferredTime = "Today Evening",
            status = RequestStatus.PENDING,
            timestamp = "10 mins ago"
        )
    )
}