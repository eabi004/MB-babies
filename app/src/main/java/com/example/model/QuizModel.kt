package com.example.model

enum class QuizSubject(val displayName: String, val emoji: String) {
    SCIENCE("Science", "🔬"),
    SOCIAL_STUDIES("Social Studies", "🌍"),
    MATH("Math", "🔢"),
    ENGLISH("English", "🔤"),
    ALL("All Subjects", "🌟")
}

data class QuizQuestion(
    val id: String,
    val subject: QuizSubject,
    val topic: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val emoji: String,
    val explanation: String,
    val isAiGenerated: Boolean = false
)

val defaultQuizQuestions = listOf(
    // ==========================================
    // SCIENCE QUESTIONS (CBSE 5-7 Yrs) - 25 Questions
    // ==========================================
    QuizQuestion(
        id = "sci_1",
        subject = QuizSubject.SCIENCE,
        topic = "Solar System",
        question = "Which planet do we live on?",
        options = listOf("Earth", "Mars", "Jupiter", "Venus"),
        correctIndex = 0,
        emoji = "🌍",
        explanation = "Earth is our home planet! It has land, water, and air for living things."
    ),
    QuizQuestion(
        id = "sci_2",
        subject = QuizSubject.SCIENCE,
        topic = "Plants & Trees",
        question = "What do plants need to make their food?",
        options = listOf("Sunlight & Water", "Ice Cream", "Juice", "Candy"),
        correctIndex = 0,
        emoji = "🌱",
        explanation = "Plants use sunlight, water, and air to make their own food through photosynthesis!"
    ),
    QuizQuestion(
        id = "sci_3",
        subject = QuizSubject.SCIENCE,
        topic = "Animals",
        question = "Which animal is known as the King of the Jungle?",
        options = listOf("Elephant", "Lion", "Giraffe", "Tiger"),
        correctIndex = 1,
        emoji = "🦁",
        explanation = "The Lion is called the King of the Jungle because of its strength and majestic mane!"
    ),
    QuizQuestion(
        id = "sci_4",
        subject = QuizSubject.SCIENCE,
        topic = "Human Body",
        question = "Which body part helps us pump blood throughout our body?",
        options = listOf("Lungs", "Heart", "Stomach", "Brain"),
        correctIndex = 1,
        emoji = "🫀",
        explanation = "Our Heart beats continuously to pump blood and oxygen everywhere in our body!"
    ),
    QuizQuestion(
        id = "sci_5",
        subject = QuizSubject.SCIENCE,
        topic = "Weather & Water",
        question = "What falls from clouds when it rains?",
        options = listOf("Water drops", "Snowflakes", "Milk", "Juice"),
        correctIndex = 0,
        emoji = "🌧️",
        explanation = "Clouds are made of tiny water drops. When they become heavy, it rains water!"
    ),
    QuizQuestion(
        id = "sci_6",
        subject = QuizSubject.SCIENCE,
        topic = "Five Senses",
        question = "Which sense organ helps us hear music and sounds?",
        options = listOf("Eyes", "Ears", "Nose", "Tongue"),
        correctIndex = 1,
        emoji = "👂",
        explanation = "Our Ears catch sound waves so we can hear songs, voices, and birds chirping!"
    ),
    QuizQuestion(
        id = "sci_7",
        subject = QuizSubject.SCIENCE,
        topic = "States of Matter",
        question = "What happens to ice when it gets warm?",
        options = listOf("Melts into water", "Turns into rock", "Freezes harder", "Floats away"),
        correctIndex = 0,
        emoji = "🧊",
        explanation = "Ice is solid water. Heat makes ice melt into liquid water!"
    ),
    QuizQuestion(
        id = "sci_8",
        subject = QuizSubject.SCIENCE,
        topic = "Animals & Birds",
        question = "Which of these animals can fly high in the sky?",
        options = listOf("Eagle", "Dog", "Cat", "Cow"),
        correctIndex = 0,
        emoji = "🦅",
        explanation = "Eagles are birds with powerful wings that allow them to soar high in the sky!"
    ),
    QuizQuestion(
        id = "sci_9",
        subject = QuizSubject.SCIENCE,
        topic = "Human Body",
        question = "Which body part controls our thoughts and helps us think?",
        options = listOf("Brain", "Knee", "Elbow", "Finger"),
        correctIndex = 0,
        emoji = "🧠",
        explanation = "Our Brain acts like a supercomputer that controls thinking and actions!"
    ),
    QuizQuestion(
        id = "sci_10",
        subject = QuizSubject.SCIENCE,
        topic = "Five Senses",
        question = "Which sense organ helps us smell flowers and food?",
        options = listOf("Nose", "Ears", "Eyes", "Skin"),
        correctIndex = 0,
        emoji = "👃",
        explanation = "Our Nose helps us smell sweet fragrances as well as warn us of bad odors!"
    ),
    QuizQuestion(
        id = "sci_11",
        subject = QuizSubject.SCIENCE,
        topic = "Five Senses",
        question = "Which organ do we use to taste sweet chocolate or salty chips?",
        options = listOf("Tongue", "Skin", "Ear", "Nose"),
        correctIndex = 0,
        emoji = "👅",
        explanation = "Taste buds on our Tongue help us taste sweet, sour, salty, and bitter flavors!"
    ),
    QuizQuestion(
        id = "sci_12",
        subject = QuizSubject.SCIENCE,
        topic = "Animals & Habitats",
        question = "Where does a fish live?",
        options = listOf("In Water", "In Trees", "In Caves", "On Ice"),
        correctIndex = 0,
        emoji = "🐟",
        explanation = "Fish have gills that let them breathe underwater in rivers, lakes, and oceans!"
    ),
    QuizQuestion(
        id = "sci_13",
        subject = QuizSubject.SCIENCE,
        topic = "Animals & Habitats",
        question = "Which animal carries its home (a hard shell) on its back?",
        options = listOf("Tortoise / Turtle", "Rabbit", "Deer", "Fox"),
        correctIndex = 0,
        emoji = "🐢",
        explanation = "Tortoises have a protective hard shell that guards them against danger!"
    ),
    QuizQuestion(
        id = "sci_14",
        subject = QuizSubject.SCIENCE,
        topic = "Food & Health",
        question = "Which drink gives us strong bones and teeth?",
        options = listOf("Milk", "Soda", "Coffee", "Tea"),
        correctIndex = 0,
        emoji = "🥛",
        explanation = "Milk is rich in Calcium which builds strong bones and healthy teeth!"
    ),
    QuizQuestion(
        id = "sci_15",
        subject = QuizSubject.SCIENCE,
        topic = "Living & Non-Living",
        question = "Which of the following is a LIVING thing?",
        options = listOf("Puppy", "Toy Car", "Table", "Rock"),
        correctIndex = 0,
        emoji = "🐶",
        explanation = "Living things grow, eat food, breathe, and reproduce! Puppies are living things."
    ),
    QuizQuestion(
        id = "sci_16",
        subject = QuizSubject.SCIENCE,
        topic = "Living & Non-Living",
        question = "Which of these is a NON-LIVING object?",
        options = listOf("Plastic Pencil", "Baby Bird", "Green Tree", "Butterfly"),
        correctIndex = 0,
        emoji = "✏️",
        explanation = "Pencils do not grow or breathe, making them non-living objects!"
    ),
    QuizQuestion(
        id = "sci_17",
        subject = QuizSubject.SCIENCE,
        topic = "Plants",
        question = "Which part of a plant grows under the soil and absorbs water?",
        options = listOf("Roots", "Flowers", "Leaves", "Fruit"),
        correctIndex = 0,
        emoji = "🪴",
        explanation = "Roots anchor the plant in the soil and absorb water and essential minerals!"
    ),
    QuizQuestion(
        id = "sci_18",
        subject = QuizSubject.SCIENCE,
        topic = "Plants",
        question = "Which green part of a plant catches sunlight?",
        options = listOf("Leaves", "Roots", "Trunk", "Seeds"),
        correctIndex = 0,
        emoji = "🍃",
        explanation = "Leaves contain chlorophyll which catches sunlight to generate plant food!"
    ),
    QuizQuestion(
        id = "sci_19",
        subject = QuizSubject.SCIENCE,
        topic = "Solar System",
        question = "What object shines brightly in the night sky and changes shape?",
        options = listOf("The Moon", "The Sun", "A Rainbow", "A Cloud"),
        correctIndex = 0,
        emoji = "🌙",
        explanation = "The Moon orbits Earth and reflects sunlight in different phases each night!"
    ),
    QuizQuestion(
        id = "sci_20",
        subject = QuizSubject.SCIENCE,
        topic = "Weather & Seasons",
        question = "In which season do we wear warm woolen sweaters and jackets?",
        options = listOf("Winter", "Summer", "Monsoon / Rainy", "Spring"),
        correctIndex = 0,
        emoji = "❄️",
        explanation = "Winter is cold, so we wear thick woolen clothes to keep our body warm!"
    ),
    QuizQuestion(
        id = "sci_21",
        subject = QuizSubject.SCIENCE,
        topic = "Weather & Seasons",
        question = "What colorful arch appears in the sky after rain when the sun comes out?",
        options = listOf("Rainbow", "Comet", "Lightning", "Tornado"),
        correctIndex = 0,
        emoji = "🌈",
        explanation = "A Rainbow has 7 beautiful colors created by sunlight refracting through raindrops!"
    ),
    QuizQuestion(
        id = "sci_22",
        subject = QuizSubject.SCIENCE,
        topic = "Animals & Young Ones",
        question = "What is a baby dog called?",
        options = listOf("Puppy", "Kitten", "Calf", "Cub"),
        correctIndex = 0,
        emoji = "🐕",
        explanation = "A baby dog is called a Puppy! Kittens are baby cats, and Calves are baby cows."
    ),
    QuizQuestion(
        id = "sci_23",
        subject = QuizSubject.SCIENCE,
        topic = "Animals & Young Ones",
        question = "What is a baby cat called?",
        options = listOf("Kitten", "Chick", "Duckling", "Foal"),
        correctIndex = 0,
        emoji = "🐱",
        explanation = "A baby cat is called a Kitten!"
    ),
    QuizQuestion(
        id = "sci_24",
        subject = QuizSubject.SCIENCE,
        topic = "Animals & Young Ones",
        question = "What is a baby hen called?",
        options = listOf("Chick", "Puppy", "Piglet", "Lamb"),
        correctIndex = 0,
        emoji = "🐤",
        explanation = "A baby hen or chicken is called a Chick!"
    ),
    QuizQuestion(
        id = "sci_25",
        subject = QuizSubject.SCIENCE,
        topic = "Water & Air",
        question = "What invisible gas do all humans need to breathe to stay alive?",
        options = listOf("Oxygen", "Smoke", "Steam", "Dust"),
        correctIndex = 0,
        emoji = "💨",
        explanation = "Clean air contains Oxygen which our lungs take in with every breath!"
    ),

    // ==========================================
    // SOCIAL STUDIES QUESTIONS (CBSE 5-7 Yrs) - 25 Questions
    // ==========================================
    QuizQuestion(
        id = "soc_1",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Community Helpers",
        question = "Who helps put out fires and keeps us safe from danger?",
        options = listOf("Firefighter", "Baker", "Painter", "Tailor"),
        correctIndex = 0,
        emoji = "👨‍🚒",
        explanation = "Firefighters drive big red fire engines to extinguish fires and rescue people!"
    ),
    QuizQuestion(
        id = "soc_2",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Maps & Globe",
        question = "What shape is a globe, which represents our planet Earth?",
        options = listOf("Round Sphere", "Flat Square", "Triangle", "Star"),
        correctIndex = 0,
        emoji = "🌐",
        explanation = "A globe is a round sphere, just like the real shape of Earth!"
    ),
    QuizQuestion(
        id = "soc_3",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Safety & Rules",
        question = "What color light on a traffic signal tells vehicles to STOP?",
        options = listOf("Red", "Green", "Yellow", "Blue"),
        correctIndex = 0,
        emoji = "🚦",
        explanation = "Red means STOP, Yellow means GET READY, and Green means GO safely!"
    ),
    QuizQuestion(
        id = "soc_4",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Good Habits",
        question = "How many times a day should we brush our teeth to keep them clean?",
        options = listOf("Twice a day", "Once a month", "Never", "Ten times"),
        correctIndex = 0,
        emoji = "🪥",
        explanation = "Brushing in the morning and before bedtime keeps your smile sparkling clean!"
    ),
    QuizQuestion(
        id = "soc_5",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Community Helpers",
        question = "Who helps us learn new things at school?",
        options = listOf("Teacher", "Chef", "Pilot", "Gardener"),
        correctIndex = 0,
        emoji = "👩‍🏫",
        explanation = "Teachers guide us in reading, writing, math, science, and good manners!"
    ),
    QuizQuestion(
        id = "soc_6",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Family & Home",
        question = "Who are our parents' parents?",
        options = listOf("Grandparents", "Uncles", "Cousins", "Neighbors"),
        correctIndex = 0,
        emoji = "👵",
        explanation = "Grandmothers and Grandfathers are our parents' loving parents!"
    ),
    QuizQuestion(
        id = "soc_7",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Transport",
        question = "Which vehicle travels on tracks/rails to carry many passengers?",
        options = listOf("Train", "Bicycle", "Car", "Helicopter"),
        correctIndex = 0,
        emoji = "🚂",
        explanation = "Trains run on iron rails and can travel long distances with many carriages!"
    ),
    QuizQuestion(
        id = "soc_8",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Environment",
        question = "Where should we throw garbage and food wrappers?",
        options = listOf("Trash bin", "On the floor", "In the river", "Out the window"),
        correctIndex = 0,
        emoji = "🗑️",
        explanation = "Always put trash in dustbins to keep our streets and parks clean and green!"
    ),
    QuizQuestion(
        id = "soc_9",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Community Helpers",
        question = "Who treats us when we are sick or injured?",
        options = listOf("Doctor", "Carpenter", "Electrician", "Cobbler"),
        correctIndex = 0,
        emoji = "🩺",
        explanation = "Doctors and Nurses examine us, prescribe medicines, and help us get healthy!"
    ),
    QuizQuestion(
        id = "soc_10",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Community Helpers",
        question = "Who delivers letters and packages to our doorstep?",
        options = listOf("Postman / Courier", "Fisherman", "Mechanic", "Plumber"),
        correctIndex = 0,
        emoji = "📫",
        explanation = "Postmen bring letters, greeting cards, and parcels to our homes!"
    ),
    QuizQuestion(
        id = "soc_11",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Community Helpers",
        question = "Who grows crops, grains, and vegetables for us to eat?",
        options = listOf("Farmer", "Pilot", "Dancer", "Singer"),
        correctIndex = 0,
        emoji = "👨‍🌾",
        explanation = "Farmers work hard in fields to grow rice, wheat, fruits, and vegetables!"
    ),
    QuizQuestion(
        id = "soc_12",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Community Helpers",
        question = "Who flies airplanes high up in the sky?",
        options = listOf("Pilot", "Driver", "Sailor", "Conductor"),
        correctIndex = 0,
        emoji = "🧑‍✈️",
        explanation = "Pilots navigate airplanes safely through the clouds from city to city!"
    ),
    QuizQuestion(
        id = "soc_13",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "National Symbols of India",
        question = "What is the National Animal of India?",
        options = listOf("Tiger", "Elephant", "Lion", "Leopard"),
        correctIndex = 0,
        emoji = "🐅",
        explanation = "The Bengal Tiger is the proud National Animal of India!"
    ),
    QuizQuestion(
        id = "soc_14",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "National Symbols of India",
        question = "What is the National Bird of India with beautiful colorful feathers?",
        options = listOf("Peacock", "Parrot", "Pigeon", "Sparrow"),
        correctIndex = 0,
        emoji = "🦚",
        explanation = "The Peacock is India's National Bird, famous for its magnificent tail feathers!"
    ),
    QuizQuestion(
        id = "soc_15",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "National Symbols of India",
        question = "What is the National Flower of India that grows in water?",
        options = listOf("Lotus", "Rose", "Sunflower", "Jasmine"),
        correctIndex = 0,
        emoji = "🪷",
        explanation = "The Lotus flower is India's National Flower symbolizing purity!"
    ),
    QuizQuestion(
        id = "soc_16",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "National Symbols of India",
        question = "How many colors are there in the Indian Tricolor National Flag (Tiranga)?",
        options = listOf("3 Colors (Saffron, White, Green)", "2 Colors", "5 Colors", "7 Colors"),
        correctIndex = 0,
        emoji = "🇮🇳",
        explanation = "The Indian flag has 3 horizontal bands: Saffron, White, and Green with Ashoka Chakra!"
    ),
    QuizQuestion(
        id = "soc_17",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Good Manners",
        question = "What polite magic word should we say when we ask for help or something we need?",
        options = listOf("Please", "Go away", "Mine", "Hurry up"),
        correctIndex = 0,
        emoji = "🙏",
        explanation = "Saying 'Please' shows respect and good manners whenever you request something!"
    ),
    QuizQuestion(
        id = "soc_18",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Good Manners",
        question = "What polite word should we say if we accidentally hurt someone or make a mistake?",
        options = listOf("Sorry", "Hello", "Bye", "Yay"),
        correctIndex = 0,
        emoji = "🥺",
        explanation = "Saying 'Sorry' shows kindness and helps heal hurt feelings!"
    ),
    QuizQuestion(
        id = "soc_19",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Good Manners",
        question = "What magic words do we say when someone gives us a present or helps us?",
        options = listOf("Thank You!", "No way", "Give me more", "Nothing"),
        correctIndex = 0,
        emoji = "🎁",
        explanation = "Saying 'Thank You' shows appreciation and brightens people's day!"
    ),
    QuizQuestion(
        id = "soc_20",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Road Safety",
        question = "Where should pedestrians cross the busy road safely?",
        options = listOf("On the Zebra Crossing", "Middle of the road", "Behind a truck", "Anywhere"),
        correctIndex = 0,
        emoji = "🦓",
        explanation = "Zebra Crossings have black and white stripes where drivers stop for pedestrians!"
    ),
    QuizQuestion(
        id = "soc_21",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Transport",
        question = "Which mode of transport floats on water across lakes and seas?",
        options = listOf("Ship / Boat", "Bus", "Helicopter", "Motorcycle"),
        correctIndex = 0,
        emoji = "🚢",
        explanation = "Ships and boats carry passengers and goods safely across water bodies!"
    ),
    QuizQuestion(
        id = "soc_22",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Festivals",
        question = "Which Indian festival is known as the Festival of Lights?",
        options = listOf("Diwali", "Holi", "Eid", "Christmas"),
        correctIndex = 0,
        emoji = "🪔",
        explanation = "Diwali is celebrated with diyas, lamps, fireworks, and delicious sweets!"
    ),
    QuizQuestion(
        id = "soc_23",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Festivals",
        question = "Which joyous festival is known as the Festival of Colors?",
        options = listOf("Holi", "Diwali", "Onam", "Pongal"),
        correctIndex = 0,
        emoji = "🎨",
        explanation = "Holi is celebrated with vibrant water colors, powder, music, and fun!"
    ),
    QuizQuestion(
        id = "soc_24",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Neighborhood",
        question = "Where do we go to buy fresh vegetables, fruits, and groceries?",
        options = listOf("Market / Supermarket", "Hospital", "Post Office", "Fire Station"),
        correctIndex = 0,
        emoji = "🛒",
        explanation = "Markets and shops sell fresh food, groceries, and daily supplies!"
    ),
    QuizQuestion(
        id = "soc_25",
        subject = QuizSubject.SOCIAL_STUDIES,
        topic = "Neighborhood",
        question = "Where do children go to play on swings, slides, and seesaws?",
        options = listOf("Park / Playground", "Bank", "Police Station", "Airport"),
        correctIndex = 0,
        emoji = "🛝",
        explanation = "Parks offer green grass, swings, and outdoor exercise areas for kids!"
    ),

    // ==========================================
    // MATH & LOGIC QUESTIONS (CBSE 5-7 Yrs) - 25 Questions
    // ==========================================
    QuizQuestion(
        id = "math_1",
        subject = QuizSubject.MATH,
        topic = "Counting",
        question = "What number comes right after 9?",
        options = listOf("10", "8", "11", "12"),
        correctIndex = 0,
        emoji = "🔢",
        explanation = "When counting 7, 8, 9... the very next number is 10!"
    ),
    QuizQuestion(
        id = "math_2",
        subject = QuizSubject.MATH,
        topic = "Addition",
        question = "What is 2 + 3?",
        options = listOf("5", "4", "6", "3"),
        correctIndex = 0,
        emoji = "➕",
        explanation = "If you have 2 apples and get 3 more apples, you have 5 apples in total!"
    ),
    QuizQuestion(
        id = "math_3",
        subject = QuizSubject.MATH,
        topic = "Addition",
        question = "What is 5 + 5?",
        options = listOf("10", "8", "12", "15"),
        correctIndex = 0,
        emoji = "✋",
        explanation = "5 fingers on one hand + 5 fingers on the other hand = 10 fingers!"
    ),
    QuizQuestion(
        id = "math_4",
        subject = QuizSubject.MATH,
        topic = "Subtraction",
        question = "If you have 4 balloons and 1 pops, how many balloons are left?",
        options = listOf("3", "2", "4", "5"),
        correctIndex = 0,
        emoji = "🎈",
        explanation = "4 minus 1 equals 3 balloons remaining!"
    ),
    QuizQuestion(
        id = "math_5",
        subject = QuizSubject.MATH,
        topic = "Shapes",
        question = "Which shape has 3 sides and 3 corners?",
        options = listOf("Triangle", "Square", "Circle", "Rectangle"),
        correctIndex = 0,
        emoji = "🔺",
        explanation = "Triangles have 3 straight sides and 3 sharp corners!"
    ),
    QuizQuestion(
        id = "math_6",
        subject = QuizSubject.MATH,
        topic = "Shapes",
        question = "Which shape is perfectly round with zero corners?",
        options = listOf("Circle", "Square", "Triangle", "Star"),
        correctIndex = 0,
        emoji = "🔴",
        explanation = "A Circle is a smooth curved line with no corners or straight edges!"
    ),
    QuizQuestion(
        id = "math_7",
        subject = QuizSubject.MATH,
        topic = "Shapes",
        question = "Which shape has 4 EQUAL sides?",
        options = listOf("Square", "Triangle", "Circle", "Oval"),
        correctIndex = 0,
        emoji = "🟦",
        explanation = "A Square has 4 straight sides that are all exactly the same length!"
    ),
    QuizQuestion(
        id = "math_8",
        subject = QuizSubject.MATH,
        topic = "Comparison",
        question = "Which number is GREATER: 15 or 8?",
        options = listOf("15", "8", "They are equal", "None"),
        correctIndex = 0,
        emoji = "📊",
        explanation = "15 is bigger/greater than 8!"
    ),
    QuizQuestion(
        id = "math_9",
        subject = QuizSubject.MATH,
        topic = "Comparison",
        question = "Which number is SMALLER: 3 or 9?",
        options = listOf("3", "9", "They are equal", "Zero"),
        correctIndex = 0,
        emoji = "🔍",
        explanation = "3 is less than 9!"
    ),
    QuizQuestion(
        id = "math_10",
        subject = QuizSubject.MATH,
        topic = "Clock & Time",
        question = "What instrument hangs on a wall to show us the time?",
        options = listOf("Clock", "Thermometer", "Scale", "Compass"),
        correctIndex = 0,
        emoji = "⏰",
        explanation = "Clocks have hour and minute hands to tell us what time it is!"
    ),
    QuizQuestion(
        id = "math_11",
        subject = QuizSubject.MATH,
        topic = "Patterns",
        question = "Complete the pattern: Red, Blue, Red, Blue, ____?",
        options = listOf("Red", "Blue", "Green", "Yellow"),
        correctIndex = 0,
        emoji = "🎨",
        explanation = "The pattern repeats Red then Blue! So after Blue comes Red!"
    ),
    QuizQuestion(
        id = "math_12",
        subject = QuizSubject.MATH,
        topic = "Patterns",
        question = "What number comes next: 2, 4, 6, 8, ____?",
        options = listOf("10", "9", "11", "12"),
        correctIndex = 0,
        emoji = "🔢",
        explanation = "Counting by 2s: 2, 4, 6, 8, 10!"
    ),
    QuizQuestion(
        id = "math_13",
        subject = QuizSubject.MATH,
        topic = "Money",
        question = "What currency coins and notes are used in India?",
        options = listOf("Rupees (₹)", "Dollars ($)", "Euros (€)", "Yen (¥)"),
        correctIndex = 0,
        emoji = "🪙",
        explanation = "India uses the Indian Rupee (₹) as its national currency!"
    ),
    QuizQuestion(
        id = "math_14",
        subject = QuizSubject.MATH,
        topic = "Money",
        question = "If a candy costs ₹5 and you give a ₹10 coin, how much change do you get back?",
        options = listOf("₹5", "₹2", "₹1", "₹0"),
        correctIndex = 0,
        emoji = "💵",
        explanation = "10 minus 5 = 5 Rupees change returned!"
    ),
    QuizQuestion(
        id = "math_15",
        subject = QuizSubject.MATH,
        topic = "Addition",
        question = "What is 7 + 1?",
        options = listOf("8", "7", "9", "6"),
        correctIndex = 0,
        emoji = "➕",
        explanation = "Adding 1 to any number gives the very next number! 7 + 1 = 8."
    ),
    QuizQuestion(
        id = "math_16",
        subject = QuizSubject.MATH,
        topic = "Addition",
        question = "What is 4 + 4?",
        options = listOf("8", "6", "10", "7"),
        correctIndex = 0,
        emoji = "➕",
        explanation = "4 plus 4 equals 8!"
    ),
    QuizQuestion(
        id = "math_17",
        subject = QuizSubject.MATH,
        topic = "Subtraction",
        question = "What is 10 - 2?",
        options = listOf("8", "7", "9", "6"),
        correctIndex = 0,
        emoji = "➖",
        explanation = "10 take away 2 leaves 8!"
    ),
    QuizQuestion(
        id = "math_18",
        subject = QuizSubject.MATH,
        topic = "Measurement",
        question = "Which tool do we use to draw a straight line or measure short lengths?",
        options = listOf("Ruler / Scale", "Clock", "Weight balance", "Cup"),
        correctIndex = 0,
        emoji = "📏",
        explanation = "A Ruler has centimeter markings to draw clean straight lines!"
    ),
    QuizQuestion(
        id = "math_19",
        subject = QuizSubject.MATH,
        topic = "Weight",
        question = "Which object is HEAVIER: a heavy Elephant or a light Feather?",
        options = listOf("Elephant", "Feather", "Both equal", "Neither"),
        correctIndex = 0,
        emoji = "🐘",
        explanation = "Elephants weigh thousands of kilograms while feathers are super light!"
    ),
    QuizQuestion(
        id = "math_20",
        subject = QuizSubject.MATH,
        topic = "Fractions / Logic",
        question = "If you cut a sweet pizza into 2 equal pieces, what is 1 piece called?",
        options = listOf("Half", "Quarter", "Whole", "Triple"),
        correctIndex = 0,
        emoji = "🍕",
        explanation = "Cutting something into 2 equal parts makes 2 Halves!"
    ),
    QuizQuestion(
        id = "math_21",
        subject = QuizSubject.MATH,
        topic = "Logic & Position",
        question = "Where is the Sun during noon time?",
        options = listOf("High UP in the sky", "DOWN under ground", "Under the ocean", "Behind trees"),
        correctIndex = 0,
        emoji = "☀️",
        explanation = "At noon (12 PM), the sun is highest UP in the sky!"
    ),
    QuizQuestion(
        id = "math_22",
        subject = QuizSubject.MATH,
        topic = "Addition",
        question = "What is 6 + 3?",
        options = listOf("9", "8", "10", "7"),
        correctIndex = 0,
        emoji = "➕",
        explanation = "6 + 3 = 9!"
    ),
    QuizQuestion(
        id = "math_23",
        subject = QuizSubject.MATH,
        topic = "Counting Pairs",
        question = "How many items are in a 'Pair' of shoes or gloves?",
        options = listOf("2", "4", "1", "3"),
        correctIndex = 0,
        emoji = "👟",
        explanation = "A 'Pair' always means 2 matching items (like 2 shoes or 2 socks)!"
    ),
    QuizQuestion(
        id = "math_24",
        subject = QuizSubject.MATH,
        topic = "Counting Dozens",
        question = "How many bananas are in 1 Dozen bananas?",
        options = listOf("12", "10", "6", "15"),
        correctIndex = 0,
        emoji = "🍌",
        explanation = "1 Dozen always equals 12 items!"
    ),
    QuizQuestion(
        id = "math_25",
        subject = QuizSubject.MATH,
        topic = "Zero Property",
        question = "What is 5 + 0?",
        options = listOf("5", "0", "50", "1"),
        correctIndex = 0,
        emoji = "0️⃣",
        explanation = "Adding 0 to any number leaves the number unchanged! 5 + 0 = 5."
    ),

    // ==========================================
    // ENGLISH & PHONICS QUESTIONS (CBSE 5-7 Yrs) - 25 Questions
    // ==========================================
    QuizQuestion(
        id = "eng_1",
        subject = QuizSubject.ENGLISH,
        topic = "Vowels",
        question = "How many VOWELS are in the English alphabet (A, E, I, O, U)?",
        options = listOf("5 Vowels", "10 Vowels", "26 Vowels", "3 Vowels"),
        correctIndex = 0,
        emoji = "🔤",
        explanation = "The 5 vowels are A, E, I, O, and U!"
    ),
    QuizQuestion(
        id = "eng_2",
        subject = QuizSubject.ENGLISH,
        topic = "Opposites",
        question = "What is the opposite of HIGH?",
        options = listOf("Low", "Tall", "Big", "Fast"),
        correctIndex = 0,
        emoji = "↕️",
        explanation = "The opposite of High is Low!"
    ),
    QuizQuestion(
        id = "eng_3",
        subject = QuizSubject.ENGLISH,
        topic = "Opposites",
        question = "What is the opposite of HOT?",
        options = listOf("Cold", "Warm", "Dry", "Bright"),
        correctIndex = 0,
        emoji = "🧊",
        explanation = "The opposite of Hot is Cold!"
    ),
    QuizQuestion(
        id = "eng_4",
        subject = QuizSubject.ENGLISH,
        topic = "Opposites",
        question = "What is the opposite of HAPPY?",
        options = listOf("Sad", "Glad", "Joyful", "Fun"),
        correctIndex = 0,
        emoji = "😢",
        explanation = "The opposite of Happy is Sad!"
    ),
    QuizQuestion(
        id = "eng_5",
        subject = QuizSubject.ENGLISH,
        topic = "Rhyming Words",
        question = "Which word rhymes with CAT?",
        options = listOf("HAT", "DOG", "SUN", "PIN"),
        correctIndex = 0,
        emoji = "🧢",
        explanation = "CAT and HAT end with the same 'at' sound!"
    ),
    QuizQuestion(
        id = "eng_6",
        subject = QuizSubject.ENGLISH,
        topic = "Rhyming Words",
        question = "Which word rhymes with SUN?",
        options = listOf("RUN", "MOON", "STAR", "SKY"),
        correctIndex = 0,
        emoji = "🏃",
        explanation = "SUN and RUN share the 'un' rhyming sound!"
    ),
    QuizQuestion(
        id = "eng_7",
        subject = QuizSubject.ENGLISH,
        topic = "Animal Sounds",
        question = "What sound does a dog make?",
        options = listOf("Woof / Bark", "Meow", "Moo", "Quack"),
        correctIndex = 0,
        emoji = "🐶",
        explanation = "Dogs bark 'Woof Woof!' Cats say 'Meow'!"
    ),
    QuizQuestion(
        id = "eng_8",
        subject = QuizSubject.ENGLISH,
        topic = "Animal Sounds",
        question = "What sound does a cow make on a farm?",
        options = listOf("Moo Moo", "Oink Oink", "Neigh", "Bleat"),
        correctIndex = 0,
        emoji = "🐄",
        explanation = "Cows say 'Moo Moo'!"
    ),
    QuizQuestion(
        id = "eng_9",
        subject = QuizSubject.ENGLISH,
        topic = "Plurals",
        question = "What is the plural of ONE APPLE?",
        options = listOf("Two APPLES", "Two Appling", "Two Apple", "Two Applesss"),
        correctIndex = 0,
        emoji = "🍎",
        explanation = "We add 's' at the end to make it plural: One Apple -> Two Apples!"
    ),
    QuizQuestion(
        id = "eng_10",
        subject = QuizSubject.ENGLISH,
        topic = "Plurals",
        question = "What is the plural of ONE CAT?",
        options = listOf("CATS", "Cates", "Cating", "Kitties"),
        correctIndex = 0,
        emoji = "🐱",
        explanation = "One Cat -> Many Cats!"
    ),
    QuizQuestion(
        id = "eng_11",
        subject = QuizSubject.ENGLISH,
        topic = "Phonics",
        question = "Which letter makes the 'B' sound in Ball?",
        options = listOf("B", "D", "P", "T"),
        correctIndex = 0,
        emoji = "⚽",
        explanation = "Letter B stands for Ball, Bear, and Butterfly!"
    ),
    QuizQuestion(
        id = "eng_12",
        subject = QuizSubject.ENGLISH,
        topic = "Phonics",
        question = "Which vowel starts the word APPLE?",
        options = listOf("A", "E", "I", "O"),
        correctIndex = 0,
        emoji = "🍎",
        explanation = "A is for Apple!"
    ),
    QuizQuestion(
        id = "eng_13",
        subject = QuizSubject.ENGLISH,
        topic = "Phonics",
        question = "Which vowel starts the word ELEPHANT?",
        options = listOf("E", "A", "U", "O"),
        correctIndex = 0,
        emoji = "🐘",
        explanation = "E is for Elephant!"
    ),
    QuizQuestion(
        id = "eng_14",
        subject = QuizSubject.ENGLISH,
        topic = "Phonics",
        question = "Which vowel starts the word ICE CREAM?",
        options = listOf("I", "E", "A", "U"),
        correctIndex = 0,
        emoji = "🍦",
        explanation = "I is for Ice Cream!"
    ),
    QuizQuestion(
        id = "eng_15",
        subject = QuizSubject.ENGLISH,
        topic = "Phonics",
        question = "Which vowel starts the word ORANGE?",
        options = listOf("O", "U", "E", "A"),
        correctIndex = 0,
        emoji = "🍊",
        explanation = "O is for Orange!"
    ),
    QuizQuestion(
        id = "eng_16",
        subject = QuizSubject.ENGLISH,
        topic = "Phonics",
        question = "Which vowel starts the word UMBRELLA?",
        options = listOf("U", "A", "E", "I"),
        correctIndex = 0,
        emoji = "☂️",
        explanation = "U is for Umbrella!"
    ),
    QuizQuestion(
        id = "eng_17",
        subject = QuizSubject.ENGLISH,
        topic = "Action Words / Verbs",
        question = "What action word describes what fish do in water?",
        options = listOf("Swim", "Fly", "Run", "Jump"),
        correctIndex = 0,
        emoji = "🏊",
        explanation = "Fish swim gracefully through water using their fins!"
    ),
    QuizQuestion(
        id = "eng_18",
        subject = QuizSubject.ENGLISH,
        topic = "Action Words / Verbs",
        question = "What action word describes what birds do in the sky?",
        options = listOf("Fly", "Swim", "Crawl", "Drive"),
        correctIndex = 0,
        emoji = "🕊️",
        explanation = "Birds flap their wings to Fly in the sky!"
    ),
    QuizQuestion(
        id = "eng_19",
        subject = QuizSubject.ENGLISH,
        topic = "Action Words / Verbs",
        question = "What action word describes what kangaroos and frogs do?",
        options = listOf("Hop / Jump", "Sleep", "Write", "Cook"),
        correctIndex = 0,
        emoji = "🦘",
        explanation = "Kangaroos and frogs hop and jump high!"
    ),
    QuizQuestion(
        id = "eng_20",
        subject = QuizSubject.ENGLISH,
        topic = "Capital Letters",
        question = "Which letter is a CAPITAL (uppercase) letter?",
        options = listOf("A", "a", "b", "c"),
        correctIndex = 0,
        emoji = "🔤",
        explanation = "'A' is an Uppercase / Capital letter! 'a' is lowercase."
    ),
    QuizQuestion(
        id = "eng_21",
        subject = QuizSubject.ENGLISH,
        topic = "Sight Words",
        question = "Complete the sentence: 'The sun is ____ in the sky.'",
        options = listOf("shining", "swimming", "barking", "eating"),
        correctIndex = 0,
        emoji = "☀️",
        explanation = "The sun is shining brightly in the sky!"
    ),
    QuizQuestion(
        id = "eng_22",
        subject = QuizSubject.ENGLISH,
        topic = "Prepositions",
        question = "Where is the bird if it sits ON TOP of a tree branch?",
        options = listOf("On the branch", "Under ground", "Inside a box", "Behind the wall"),
        correctIndex = 0,
        emoji = "🐦",
        explanation = "'On' means positioned on top of a surface!"
    ),
    QuizQuestion(
        id = "eng_23",
        subject = QuizSubject.ENGLISH,
        topic = "Color Vocabulary",
        question = "What color is ripe sweet BANANA?",
        options = listOf("Yellow", "Blue", "Purple", "Black"),
        correctIndex = 0,
        emoji = "🍌",
        explanation = "Ripe bananas have a bright Yellow skin!"
    ),
    QuizQuestion(
        id = "eng_24",
        subject = QuizSubject.ENGLISH,
        topic = "Color Vocabulary",
        question = "What color is lush garden GRASS?",
        options = listOf("Green", "Pink", "Red", "Orange"),
        correctIndex = 0,
        emoji = "🌾",
        explanation = "Healthy grass and plant leaves are Green!"
    ),
    QuizQuestion(
        id = "eng_25",
        subject = QuizSubject.ENGLISH,
        topic = "Pronouns",
        question = "Which pronoun do we use for a BOY or MAN?",
        options = listOf("He", "She", "It", "They"),
        correctIndex = 0,
        emoji = "👦",
        explanation = "We use 'He' for males (boy/man) and 'She' for females (girl/woman)!"
    )
)
