package com.goransson

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val color: Int,
    val accentColor: Int,
    val description: String,
    val songs: List<Song>
)

data class Song(
    val id: Int,
    val title: String,
    val duration: String,
    val frequency: Double
)

object MusicData {
    val movies: List<Movie> = listOf(
        Movie(
            id = 1,
            title = "Black Panther",
            year = 2018,
            color = 0xFF3B0764.toInt(),
            accentColor = 0xFFE9C46A.toInt(),
            description = "Academy Award — Best Original Score",
            songs = listOf(
                Song(1,  "Warrior Falls",             "4:42", 220.00),
                Song(2,  "Ancestral Plane",           "3:15", 246.94),
                Song(3,  "A Clue",                    "2:58", 261.63),
                Song(4,  "First Official Challenge",  "5:23", 293.66),
                Song(5,  "Let the Ancestors Speak",   "6:01", 329.63),
                Song(6,  "Is This Wakanda?",          "3:17", 349.23)
            )
        ),
        Movie(
            id = 2,
            title = "Tenet",
            year = 2020,
            color = 0xFF0A1628.toInt(),
            accentColor = 0xFF7EB8F7.toInt(),
            description = "Grammy Award — Best Score Soundtrack",
            songs = listOf(
                Song(7,  "The Algorithm",             "5:14", 392.00),
                Song(8,  "Posterity",                 "4:33", 440.00),
                Song(9,  "Backwards POV",             "3:47", 493.88),
                Song(10, "Pull",                      "6:02", 523.25),
                Song(11, "The Protagonist",           "4:51", 587.33),
                Song(12, "Rainy Night in Tallinn",    "3:29", 659.25)
            )
        ),
        Movie(
            id = 3,
            title = "Oppenheimer",
            year = 2023,
            color = 0xFF3D0000.toInt(),
            accentColor = 0xFFFBBF24.toInt(),
            description = "Academy Award — Best Original Score",
            songs = listOf(
                Song(13, "Can You Hear the Music",    "5:18", 174.61),
                Song(14, "Fission",                   "4:07", 196.00),
                Song(15, "Destroyer of Worlds",       "6:44", 207.65),
                Song(16, "Trinity",                   "3:52", 233.08),
                Song(17, "American Prometheus",       "5:09", 246.94),
                Song(18, "Quantum Mechanics",         "4:23", 261.63)
            )
        )
    )
}
