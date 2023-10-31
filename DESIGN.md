# General project design

## Core idea

The game is played between two players. It starts with a single random word. The players take turns appending the next word to the common line, such that the ending of the line is the beginning of the next word. Example: `snake -> s`**`nake`**`d -> snak`**`ed`**`ible -> ...`. The player gets the score equal to the length of the overlap. The individual words cannot repeat, and forms and inflections cannot be used either. To keep the game going, each player has a limited time to make a move. The game ends after a predetermined number of moves, and the player with the highest score wins.

### Possible extensions

* More than 2 players
* Choose a dictionary
  * Important case: prohibit words ending with -tion / ...
  * Limit words to some topic
  * Different languages
* End after score difference is reached (i.e. 10 points behind is game over)
* Extra points for rare words / fully using the other player's letters

## Project scope

The game is online multiplayer. Players can either use a "quick play" option to get randomly matched with some other player with default game parameters, or can create a game with custom parameters (such as time per move, game length, public/private). Public games go into the common matching pool, and private games can only be joined via a link.

Players also can have profiles. They allow to: set an avatar, choose favorite colors, see game history.

## Architecture

The logic is quite simple:

* matching users and creating games
  * will need `User` and `Game` models, as well as a `Matcher`
* playing the game
  * nothing too complex here; will need a `Dictionary` to validate words

The backend:

* serving landing and game pages
* connecting users' actions to model logic via websockets
  * note: backend has no background process, it only responds to incoming requests
  * fine, almost no background, just one timer for each game. ~~It is a part of `Game` model though. Let's put all the state into that model~~
    * Having not just state but also temporal state in the main model will be an absolute headache. Timers should live separately.
    * They should also live on the backend, so that users cannot cheat. Note that this will require some synchronization to display them.
    * ...or we can trust that frontend timer is in sync by default?... TODO
* ~~storing users data in a DB~~
  * There really is no need to have user accounts and stuff; we only want to store very basic data like a username and a very simple avatar / color. Looking at [skribbl](skribbl.io), we can store that in cookies and not care about persistence whatsoever.

The frontend:

* provides the buttons to interact with logic :)
* should respond to incoming websocket messages

## Protocol

When the user presses "join game" button, a new websocket connection is opened. This is because a) we will need a connection for the game anyway b) the user might need to wait until they are matched and get a response after that. The possible messages are:

* (->) request matching (desired game parameters)
* (<-) matched, game created (actual game parameters)
* (->) try to make a move (string = suffix)
* (<-) move invalid (suffix does not create a word, word was already used, ...)
* (<-) move accepted
* (<-) opponent made their move (their suffix)
* (<-) game ended (reached the end, opponent resigned/disconnected, timer reached zero)
* (<->) ping-pong (frequent + timestamp)

~~Any message can also be an error message~~, and the socket can also be closed. However, these errors will be inconvenient to handle. Let's simply close the socket in case an exception is thrown and terminate the game.

Game id is not really necessary, because one socket is always connected to just one game, and the dispatch of sockets to game models on the backend can be internal.

## Other notes

Theoretically, the dictionaries can contain up to 100K words, and even an order more if they also contain inflections. The storage of strings is not going to be a problem, but the lookup might actually be. Since we operate with word parts and not whole words, the lookup will likely be slow. Some fancy preprocessing and/or algorithms may be needed in the end.
