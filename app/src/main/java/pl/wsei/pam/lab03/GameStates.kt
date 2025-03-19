package pl.wsei.pam.lab03

enum class GameStates {
    Matching, // Oczekiwanie na wybór pierwszej karty
    Match,    // Karty pasują do siebie
    NoMatch,  // Karty nie pasują do siebie
    Finished  // Gra zakończona (wszystkie pary znalezione)
}
