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
        details = "The Monstera Deliciosa, also known as the Swiss Cheese Plant, is famous for its large, distinctive split leaves. Native to the tropical rainforests of Central America, particularly southern Mexico and Panama, this iconic plant has become a beloved houseplant worldwide. The unique fenestrations (holes) in its leaves develop as the plant matures, creating its signature Swiss cheese appearance. In its natural habitat, Monstera is a climbing plant that uses aerial roots to attach to trees and can grow up to 21 meters tall. As a houseplant, it brings a dramatic tropical feel to any space and can purify the air by removing toxins. The plant's name comes from the Latin word 'monstrous,' referring to its unusually large leaves that can reach up to 90 cm in diameter.",
        careInstructions = "Water: When top 5 cm of soil are dry\nLight: Bright, indirect light\nTemp: 18-29°C\nTips: Wipe leaves to remove dust",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 1,
        name = "Pothos",
        scientificName = "Epipremnum aureum",
        imageRes = R.drawable.pothos,
        details = "Devil's Ivy is an extremely forgiving trailing plant with glossy, heart-shaped leaves that cascade beautifully from hanging baskets or shelves. Originating from the Solomon Islands in the South Pacific and French Polynesia, this resilient plant has earned its nickname 'Devil's Ivy' because it's nearly impossible to kill and stays green even in darkness. In its native tropical habitat, Pothos grows as a climbing vine that can reach 12 meters in length, wrapping around tree trunks with its aerial roots. The plant features stunning variegation in shades of green, yellow, and white, making each leaf unique. Pothos is perfect for beginners and thrives in various lighting conditions, from low light to bright indirect sun. NASA studies have shown it's highly effective at removing indoor air pollutants like formaldehyde and benzene, making it both beautiful and functional.",
        careInstructions = "Water: When soil is dry\nLight: Low to bright indirect light\nTemp: 15-29°C\nTips: Very low maintenance",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 2,
        name = "Snake Plant",
        scientificName = "Sansevieria trifasciata",
        imageRes = R.drawable.snake_plant,
        details = "The Snake Plant, also known as Mother-in-Law's Tongue or Viper's Bowstring Hemp, features striking upright, sword-like leaves with distinctive yellow margins and dark green horizontal banding patterns. Native to West Africa, from Nigeria to the Congo, this architectural plant has been cultivated for centuries and was historically used to make bowstrings due to its strong fibers. The plant's stiff, vertical growth makes it an excellent choice for modern interior design, adding height and structure to any space. Snake Plants are virtually indestructible and can survive weeks of neglect, making them perfect for beginners or frequent travelers. They're unique among houseplants because they continue to produce oxygen at night, making them ideal bedroom companions. The plant can live for decades and slowly spreads through underground rhizomes, eventually forming attractive clumps.",
        careInstructions = "Water: Only when soil is completely dry\nLight: Tolerates low light\nTemp: 13-29°C\nTips: Very drought tolerant",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 3,
        name = "Fiddle Leaf Fig",
        scientificName = "Ficus lyrata",
        imageRes = R.drawable.fiddle_leaf_fig,
        details = "The Fiddle Leaf Fig is an Instagram-famous plant known for its enormous, glossy, violin-shaped leaves that can grow up to 45 cm long. Native to the lowland tropical rainforests of Western Africa, from Cameroon to Sierra Leone, this striking tree grows naturally in humid forest understories. In the wild, it can reach heights of 12-15 meters, but as a houseplant, it typically grows 1.8-3 meters tall indoors. The plant's sculptural form and dramatic foliage have made it a designer favorite and a staple in modern interior design. While it's a popular statement plant that can transform any room into a sophisticated space, it does require more attention and consistency than beginner plants. Fiddle Leaf Figs are sensitive to changes in their environment and prefer to stay in one spot once established. The large leaves collect dust easily, so regular cleaning helps the plant photosynthesize efficiently.",
        careInstructions = "Water: When top 2.5 cm of soil is dry\nLight: Bright, indirect light\nTemp: 18-24°C\nTips: Wipe leaves regularly",
        difficulty = Difficulty.MEDIUM
    ),
    Plant(
        id = 4,
        name = "Peace Lily",
        scientificName = "Spathiphyllum",
        imageRes = R.drawable.peace_lily,
        details = "The Peace Lily is an elegant flowering plant that produces stunning white spathes (often mistaken for flowers) that elegantly arch above deep green, glossy foliage. Native to the tropical rainforests of Central and South America, as well as Southeast Asia, this graceful plant thrives in the dappled shade of the forest floor. The white 'blooms' are actually modified leaves called spathes that surround the plant's tiny true flowers. Peace Lilies are renowned for their exceptional air-purifying properties and were featured prominently in NASA's Clean Air Study for their ability to remove toxins like benzene, formaldehyde, and carbon monoxide from indoor air. One of the plant's most helpful features is its dramatic way of communicating its needs—it will visibly droop when thirsty, then perk back up within hours of watering. This makes it nearly impossible to forget when it needs water, perfect for developing good plant care habits.",
        careInstructions = "Water: Keep soil moist but not soggy\nLight: Low to medium light\nTemp: 18-27°C\nTips: Mist leaves occasionally",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 5,
        name = "ZZ Plant",
        scientificName = "Zamioculcas zamiifolia",
        imageRes = R.drawable.zz_plant,
        details = "The ZZ Plant, short for Zamioculcas zamiifolia, features thick, glossy, waxy leaves that grow in graceful, wand-like stems from underground rhizomes. Native to Eastern Africa, from Kenya to South Africa, this hardy plant grows naturally in drought-prone regions where it has evolved to store water in its thick stems and potato-like rhizomes. The plant's architectural form and deep green, almost plastic-looking foliage make it a popular choice for modern interiors and offices. ZZ Plants are nearly indestructible and can survive in extremely low light conditions where most other plants would struggle, making them perfect for windowless bathrooms or offices with only fluorescent lighting. They're also ideal for forgetful waterers, as they can go weeks or even months without water. The plant grows slowly but steadily, and mature specimens can reach 60-90 cm tall, creating an impressive display with minimal care required.",
        careInstructions = "Water: Only when soil is completely dry\nLight: Tolerates very low light\nTemp: 15-24°C\nTips: Extremely low maintenance",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 6,
        name = "Rubber Plant",
        scientificName = "Ficus elastica",
        imageRes = R.drawable.rubber_plant,
        details = "The Rubber Plant, named for the latex it produces, has large, thick, shiny leaves that can grow up to 30 cm long, creating a bold tropical statement. Native to the rainforests of India and Southeast Asia, particularly in areas from the Himalayas to Malaysia, Indonesia, and China, this plant was historically cultivated for rubber production before synthetic rubber became available. In its natural habitat, the Rubber Plant can grow into a massive tree reaching over 30 meters tall with a trunk diameter of 2 meters. As a houseplant, it's much more manageable, typically growing 1.8-3 meters indoors. The burgundy or variegated varieties add even more visual interest with pink, cream, and green marbled leaves. It's a classic houseplant that's relatively easy to care for and can live for decades, becoming a cherished family heirloom. The large leaves benefit from regular wiping to remove dust and maintain their signature glossy appearance.",
        careInstructions = "Water: When top 5 cm of soil are dry\nLight: Bright, indirect light\nTemp: 15-24°C\nTips: Wipe leaves to keep shiny",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 7,
        name = "Spider Plant",
        scientificName = "Chlorophytum comosum",
        imageRes = R.drawable.spider_plant,
        details = "The Spider Plant is a charming, adaptable plant featuring long, striped, arching leaves that form elegant rosettes, with variegation in shades of green and creamy white. Native to tropical and southern Africa, particularly South Africa, this resilient plant has become one of the world's most popular houseplants since Victorian times. What makes Spider Plants truly special is their prolific production of baby plantlets, called 'spiderettes' or 'pups,' that dangle from long stems like tiny spiders on webs—hence the name. These baby plants can be easily propagated by placing them in water or soil, making it one of the easiest plants to share with friends. Spider Plants are champion air purifiers, effectively removing formaldehyde and xylene from indoor air. They're incredibly forgiving and can tolerate a wide range of conditions, bouncing back quickly from neglect. The cascading growth habit makes them perfect for hanging baskets or display on pedestals where the babies can hang freely.",
        careInstructions = "Water: Keep soil slightly moist\nLight: Bright, indirect light\nTemp: 15-24°C\nTips: Produces baby plants",
        difficulty = Difficulty.EASY
    ),
    Plant(
        id = 8,
        name = "Chinese Money Plant",
        scientificName = "Pilea peperomioides",
        imageRes = R.drawable.pilea_peperomioides,
        details = "The Chinese Money Plant, also called the Pancake Plant or UFO Plant, features unique, perfectly round, coin-shaped leaves that sit atop tall, slender stems like a collection of green lily pads. Native to the Yunnan Province in southern China, near the Himalayas, this plant has a fascinating history—it was relatively unknown in the West until the 1980s when a Norwegian missionary brought cuttings from China and shared them throughout Scandinavia. The plant became a social media sensation in recent years due to its distinctive appearance and photogenic quality. Its circular leaves can grow up to 10 cm in diameter and have a slightly cupped shape that catches the light beautifully. The plant naturally leans toward light sources, so regular rotation (ideally weekly) ensures even growth and prevents it from becoming lopsided. Chinese Money Plants readily produce offsets or 'pups' at their base, which can be separated and shared, continuing the plant's tradition of being passed from friend to friend. Its trendy, modern look makes it a favorite among plant collectors and design enthusiasts.",
        careInstructions = "Water: When top 2.5 cm of soil is dry\nLight: Bright, indirect light\nTemp: 15-24°C\nTips: Rotate for even growth",
        difficulty = Difficulty.MEDIUM
    ),
    Plant(
        id = 9,
        name = "Philodendron Brasil",
        scientificName = "Philodendron hederaceum",
        imageRes = R.drawable.philodendron_brasil,
        details = "The Philodendron Brasil is a stunning variegated variety of the classic heartleaf philodendron, featuring glossy, heart-shaped leaves with dramatic splashes of lime yellow and chartreuse variegation against deep green. Native to Central America and the Caribbean, wild philodendrons are climbing plants that use aerial roots to scale trees in tropical rainforests, reaching impressive heights. The Brasil variety's vibrant variegation makes each leaf unique—some display bold yellow centers, others show marbled patterns, and the variegation can change based on light conditions. This fast-growing trailing plant is perfect for hanging baskets, shelves, or trained to climb a moss pole. The vines can grow several meters per year under ideal conditions, and the plant becomes more impressive with age. Philodendrons are among the easiest plants to propagate—simply cut below a node and place in water to root. Regular pruning encourages bushier growth and provides plenty of cuttings to share or expand your collection. The plant's forgiving nature and stunning appearance make it a favorite among both beginners and experienced plant parents.",
        careInstructions = "Water: When top 2.5 cm of soil is dry\nLight: Medium to bright indirect\nTemp: 18-27°C\nTips: Prune for bushiness",
        difficulty = Difficulty.EASY
    )
)
