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
    val rawResName: String   // filename (no extension) in res/raw/
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
                Song(1,  "Warrior Falls",            "4:42", "bp_warrior_falls"),
                Song(2,  "Ancestral Plane",          "3:15", "bp_ancestral_plane"),
                Song(3,  "A Clue",                   "2:58", "bp_a_clue"),
                Song(4,  "First Official Challenge", "5:23", "bp_first_challenge"),
                Song(5,  "Let the Ancestors Speak",  "6:01", "bp_let_ancestors_speak"),
                Song(6,  "Is This Wakanda?",         "3:17", "bp_is_this_wakanda")
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
                Song(7,  "The Algorithm",            "5:14", "tenet_algorithm"),
                Song(8,  "Posterity",                "4:33", "tenet_posterity"),
                Song(9,  "Backwards POV",            "3:47", "tenet_backwards_pov"),
                Song(10, "Pull",                     "6:02", "tenet_pull"),
                Song(11, "The Protagonist",          "4:51", "tenet_protagonist"),
                Song(12, "Rainy Night in Tallinn",   "3:29", "tenet_rainy_night")
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
                Song(13, "Can You Hear the Music",   "5:18", "opp_can_you_hear"),
                Song(14, "Fission",                  "4:07", "opp_fission"),
                Song(15, "Destroyer of Worlds",      "6:44", "opp_destroyer"),
                Song(16, "Trinity",                  "3:52", "opp_trinity"),
                Song(17, "American Prometheus",      "5:09", "opp_prometheus"),
                Song(18, "Quantum Mechanics",        "4:23", "opp_quantum")
            )
        )
    )
}
