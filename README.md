# Strategy-Based Board Game: Santorini

## Starting a Game
### Set Up Backend Server
Either run the Java backend by using your IDE or by typing
'''
mvn site
mvn exec:exec
'''
in the java folder

### Set Up Frontend Server
In the frontend folder, run
'''
npm install
npm run compile
npm run start
'''
This will start a server at http://localhost:3000

### Fixing Handlebars
There is an error with Handlebars working with Webpack. There is a simple fix for this error, however. After you run ```npm install```, this creates a node_modules folder within the frontend folder. Then, in that folder, find the Handlebars folder, and go to its package.json. You will find in this package.json, there is a section called ```browser```. Add to this dictionary the line ```"fs": false```.

## God Cards

Demeter

Minotaur

Pan

Apollo

Artemis

Hephaestus

Atlas

## Appendix 1: Santorini Rules

Santorini has very simple rules, but the game is very extensible. You can find the original rules [online](https://roxley.com/products/santorini). Beyond the actual board game, you can also find an App that implements the game if you want to try to play it.

In a nutshell, the rules are as follows: The game is played on a 5 by 5 grid, where each grid can contain towers consisting of blocks and domes. Two players have two workers each on any field of the grid. Throughout the game, the workers move around and build towers. The first worker to make it on top of a level-3 tower wins. Note that though the official rules require that if a player cannot further move any worker, she/he will lose, you don't need to consider this as a winning condition in this homework.

As setup, both players pick starting positions for both their workers on the grid. (For simplicity, in Homework 3 and 5, **you can assume a player (e.g. Player A) always starts first**). Players take turns. In each turn, they select one of their workers, move this worker to an adjacent unoccupied field, and afterward add a block or dome to an unoccupied adjacent field of their new position. Locations with a worker or a dome are considered occupied. Worker can only climb a maximum of one level when moving. Domes can only be built on level-3 towers. You can assume there are infinite pieces to play.

That's it. You probably want to play a few rounds to get a feel for the game mechanics. There are god powers that modify the game behavior, but those will not be relevant until Homework 5.
