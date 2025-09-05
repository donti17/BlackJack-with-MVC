# BlackJack Project Report  

**Programming Methodologies**  
**a.y. 2023/2024**  
 
## Description of the project  
The goal of the project is to implement the BlackJack game using the **MVC pattern**.  
The user who registers, by entering their nickname and choosing one of the 4 available avatars, can choose whether to play against the dealer or against 2–3 opponent players, as well as decide the number of chips to bet.  
The entire game record (**wins, losses, draws**) is saved and displayed on the home screen.  
 
## Project structure  
The project is organized into several folders:  

- **build** with all the files automatically generated during project compilation.  
- the **JBlackJack-main** folder that contains:  
  - **src** with the main packages of the project and all resources (images and audio files) located in the resources folder  
  - **resources** with the images of avatars, cards, chips and audio files  
  - **javadoc** with the documentation of each class  
  - **UML** and **report**  
 
## The MVC Pattern  
Here are the 3 main packages to adopt the design pattern:  

### **Model**  
Totally independent, with no imports from other packages, Swing libraries or JavaFX, and no image/audio management.  
It represents **DATA**: deck of cards, hands and scores of all players (player, dealer, opponents).  
And the **LOGIC**:  

- creation and management of the **52-card deck** (shuffled and drawn)  
- starting a new game, distributing cards and calculating scores  
- managing hands when drawing cards, updating scores and ace values (**1 or 11**)  
- managing dealer/bots turns (draw until reaching **at least 17**)  
- managing the end of the game, calculating and updating the result  

### **Controller**  
Acts as director for Model and View, **50% reusable**. Manages:  

- **Main** (JBlackJack class) which starts the game  
- creation and observation of model and view  
- **ActionListeners** for each user action (hit, stay, choosing opponents, avatar selection)  
- **Game Loop** with timer for state updates  

### **View**  
The **GUI**, containing graphics, audio, inputs and outputs. **0% reusable**. Main classes:  

- **AudioManager**  
- **Start**: initial screen (nickname + avatar)  
- **Home**: main screen (players + stats)  
- **Bet**: betting screen  
- **GameView**: common interface for OnePlayer, TwoPlayers, ThreePlayers  
- **AnimationCard**: abstract class for animated cards (coordinates, movement, visibility, rendering)  

---

## The Observer-Observable Pattern  
Used to synchronize **Model state** and **UI**, making the system reactive.  

- **Model** = **Observable** (methods `setChanged()`, `notifyObservers()`)  
- **View classes** = **Observers** (`update()` called automatically)  
- **Controller** coordinates via `model.addObserver()`, without directly implementing Observer  

This modular separation of **logic (Model)**, **presentation (View)**, and **coordination (Controller)** improves maintainability and readability.  

---

## Observer-Observable Cases  

| **CASE** | **OBSERVABLE – OBSERVER** | **TRIGGER** | **CONSEQUENCES AFTER CALL** | **HOW OFTEN?** |
|----------|----------------------------|-------------|-----------------------------|----------------|
| Card drawn by player | Model – GameView | `model.drawCard()` | updates player hand + starts animation | every “hit” |
| Card drawn by dealer | Model – GamePanel | `dealerTurn()` | animation + score update | every dealer card |
| Card drawn by bot | Model – GamePanel | `botTurn()` | updates bot card positions | every bot card |
| Dealer hidden card revealed | Model – GamePanel | `handleStayButton()` | card reveal animation | when player ends turn |
| End of game | Model – Controller → Home | `updateResults()` | shows win/loss/draw + updates stats | every end of game |
| Game reset | Model – GameView | user clicks "start game" | restores initial state | every reset |
| Score update | Model – GamePanel | `playerSum` or `dealerSum` modified | redraws scores in real time | every value change |
| Ace management | Model – GamePanel | `reduceAce()` | Ace = 11 or Ace = 1 | when score > 21 |
| Card animations | Model – AnimationCard | `notifyObservers()` (game loop) | card moves from deck to hand | based on timer |
| Turn change | Model – GameView | player → bot/dealer | hides **"Hit"** and **"Stay"** buttons | depends on game (1/2/3 players) |
| Score > 21 | Model – GameView | `handleHitButton()` | ends game → **“You lost!”** | immediately |

---

## GUI  
Main tools:  

- **Java Swing** → windows, panels, buttons, labels  
- **CardLayout** → navigation between screens  
- **Animations** → dealer card flip, card dealing, hit requests  

**Screens**  

- **Start** → JLabel (title, subtitle, avatar), JButton (Left, Right, Enter)  
- **Home** → JLabel (stats, avatar), JButton (1 Player, 2 Players, 3 Players)  
- **Bet** → JLabel (chips), JButton (circular bet buttons, “Go!”)  
- **GameView** → JPanel (cards), JButton (Hit, Stay, Home)  

**Animations**  

- **NormalCard** → standard display  
- **SmallerCard** → smaller animation (initial dealing, end game reveal)  
- **RotationCard** → rotation for hidden dealer cards  

---

## Game Flow  

### **Before the game**  
- main() in **JBlackJack** creates **Controller**, initializes JFrame + Start, Home, Bet, GameView  
- Start screen shown, user selects avatar + nickname (via JOptionPane)  

### **During the game**  
- User confirms bet → Controller initializes Model with chosen players → `model.startNewGame()`  
- Displays **OnePlayer/TwoPlayers/ThreePlayers** view  
- Starts **Game Loop** with timer → updates state → `model.notifyObservers()`  
- Player can **Hit** or **Stay** → dealer/bots act accordingly  

### **After the game**  
- Controller compares scores:  
  - **Win**: player > dealer and ≤ 21  
  - **Loss**: player > 21 or < dealer  
  - **Draw**: equal scores  
- Result shown on screen → user can return to **Home**  
- From Home, a new game can be started  

---
