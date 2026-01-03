package com.jan.composeset

import androidx.annotation.DrawableRes

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

data class Plant(
    val id: Int,
    val name: String,
    val scientificName: String,
    @DrawableRes val imageRes: Int,
    val details: String,
    val careInstructions: String,
    val difficulty: Difficulty
)

val plants = listOf(
    Plant(
        id = 0,
        name = "Monstera",
        scientificName = "Monstera deliciosa",
        imageRes = R.drawable.monstera,
        details = "The Monstera Deliciosa, also known as the Swiss Cheese Plant, is famous for its large, distinctive split leaves. It's a popular statement plant that brings a tropical feel to any space.",
        careInstructions = "Water: When top 2 inches of soil are dry\nLight: Bright, indirect light\nTemp: 65-85°F (18-29°C)\nTips: Wipe leaves to remove dust",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 1,
        name = "Pothos",
        scientificName = "Epipremnum aureum",
        imageRes = R.drawable.pothos,
        details = "Devil's Ivy is an extremely forgiving trailing plant with heart-shaped leaves. Perfect for beginners and thrives in various lighting conditions.",
        careInstructions = "Water: When soil is dry\nLight: Low to bright indirect light\nTemp: 60-85°F (15-29°C)\nTips: Very low maintenance",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 2,
        name = "Snake Plant",
        scientificName = "Sansevieria trifasciata",
        imageRes = R.drawable.snake_plant,
        details = "The Snake Plant features upright, sword-like leaves with distinctive patterns. It's virtually indestructible and perfect for beginners.",
        careInstructions = "Water: Only when soil is completely dry\nLight: Tolerates low light\nTemp: 55-85°F (13-29°C)\nTips: Very drought tolerant",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 3,
        name = "Fiddle Leaf Fig",
        scientificName = "Ficus lyrata",
        imageRes = R.drawable.fiddle_leaf_fig,
        details = "The Fiddle Leaf Fig is known for its large, violin-shaped leaves. It's a popular statement plant but requires more attention than others.",
        careInstructions = "Water: When top inch of soil is dry\nLight: Bright, indirect light\nTemp: 65-75°F (18-24°C)\nTips: Wipe leaves regularly",
        difficulty = Difficulty.MEDIUM
    ),
    Plant(
        id = 4,
        name = "Peace Lily",
        scientificName = "Spathiphyllum",
        imageRes = R.drawable.peace_lily,
        details = "The Peace Lily produces beautiful white blooms and is known for its air-purifying properties. It will droop when thirsty.",
        careInstructions = "Water: Keep soil moist but not soggy\nLight: Low to medium light\nTemp: 65-80°F (18-27°C)\nTips: Mist leaves occasionally",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 5,
        name = "ZZ Plant",
        scientificName = "Zamioculcas zamiifolia",
        imageRes = R.drawable.zz_plant,
        details = "The ZZ Plant features glossy, waxy leaves and is nearly indestructible. Perfect for low-light spaces and forgetful waterers.",
        careInstructions = "Water: Only when soil is completely dry\nLight: Tolerates very low light\nTemp: 60-75°F (15-24°C)\nTips: Extremely low maintenance",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 6,
        name = "Rubber Plant",
        scientificName = "Ficus elastica",
        imageRes = R.drawable.rubber_plant,
        details = "The Rubber Plant has large, shiny leaves and makes an excellent statement piece. It's a classic houseplant that's relatively easy to care for.",
        careInstructions = "Water: When top 2 inches of soil are dry\nLight: Bright, indirect light\nTemp: 60-75°F (15-24°C)\nTips: Wipe leaves to keep shiny",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 7,
        name = "Spider Plant",
        scientificName = "Chlorophytum comosum",
        imageRes = R.drawable.spider_plant,
        details = "The Spider Plant features striped, arching leaves and produces baby plantlets. It's one of the easiest plants to grow and propagate.",
        careInstructions = "Water: Keep soil slightly moist\nLight: Bright, indirect light\nTemp: 60-75°F (15-24°C)\nTips: Produces baby plants",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 8,
        name = "Chinese Money Plant",
        scientificName = "Pilea peperomioides",
        imageRes = R.drawable.pilea_peperomioides,
        details = "The Pilea has unique, round coin-shaped leaves on tall stems. It's a trendy plant with a distinctive appearance.",
        careInstructions = "Water: When top inch of soil is dry\nLight: Bright, indirect light\nTemp: 60-75°F (15-24°C)\nTips: Rotate for even growth",
        difficulty = Difficulty.MEDIUM
    ),
    Plant(
        id = 9,
        name = "Philodendron Brasil",
        scientificName = "Philodendron hederaceum",
        imageRes = R.drawable.philodendron_brasil,
        details = "The Philodendron features heart-shaped leaves with stunning yellow variegation. It's a fast-growing trailing plant.",
        careInstructions = "Water: When top inch of soil is dry\nLight: Medium to bright indirect\nTemp: 65-80°F (18-27°C)\nTips: Prune for bushiness",
        difficulty = Difficulty.EASY
    )
)
