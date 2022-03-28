import Handlebars from "handlebars"
import { Component } from 'react'
import './App.css'

var oldHref = "http://localhost:3000"
var powers: String[];
powers = ["NonGod: Play without god card.",
          "Demeter: Your worker may build one additional time, but not on the same space.",
          "Minotaur: Your worker may move into an opponent Worker's space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.",
          "Pan: You also win if your Worker moves down two or more levels."]

interface Cell {
    text: String;
    clazz: String;
    link: String;
}

interface Card {
    text: String;
    clazz: String;
    link: String;
}

interface Cells {
    template: HandlebarsTemplateDelegate<any>, 
    instructions: String,
    cells: Array<Cell>,
    playerbar: String,
    cards: Array<Card>
}

interface Props {
}

class App extends Component<Props, Cells> {
    constructor(props: Props) {
        super(props);
        this.state = {
            template: this.loadTemplate(),
            instructions: "Player A's turn:\nChoose god card",
            cells: [
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
                { text: "", clazz: "occupied", link: "" },
            ],
            playerbar: "Player A: Haven't choose play mode.\nPlayer B: Haven't choose play mode.",
            cards: [
                { text: powers[0], clazz: "playable", link: "/choosegodcard?i=0" },
                { text: powers[1], clazz: "playable", link: "/choosegodcard?i=1" },
                { text: powers[2], clazz: "playable", link: "/choosegodcard?i=2" },
                { text: powers[3], clazz: "playable", link: "/choosegodcard?i=3" },
            ]
        };
    }

    loadTemplate(): HandlebarsTemplateDelegate<any> {
        const src = document.getElementById("handlebars");
        return Handlebars.compile(src?.innerHTML, {});
    }

    convertToCell(p: any): Array<Cell> {
        const newCells: Array<Cell> = [];
        for (var i = 0; i < p["cells"].length; i++) {
            var c: Cell = {
                text: p["cells"][i]["text"],
                clazz: p["cells"][i]["clazz"],
                link: p["cells"][i]["link"],
            }
            newCells.push(c);
        }
        return newCells;
    }

    convertToCard(p: any): Array<Card> {
        const newCards: Array<Card> = [];
        for (var i = 0; i < p["cards"].length; i++) {
            var c: Card = {
                text: p["cards"][i]["text"],
                clazz: p["cards"][i]["clazz"],
                link: p["cards"][i]["link"],
            }
            newCards.push(c);
        }
        return newCards;
    }

    getFinishChooseCard(p: any): boolean {
        if (p["finishchoosecard"] === false) {
            console.log("finishchoosecard: false");
        } else if (p["finishchoosecard"] === true) {
            console.log("finishchoosecard: true");
        } else  {
            console.log("finishchoosecard: neither true nor false");
        }
        return p["finishchoosecard"];
    }

    getPlayerACard(p: any): number {
        return p["playeracard"];
    }

    getPlayerBCard(p: any): number {
        return p["playerbcard"];
    }

    getCurrPlayer(p: any): String {
        return p["currplayer"];
    }

    getCurrWorker(p: any): number {
        return p["currworker"];
    }
    
    getFinishInitPos(p: any): boolean {
        return p["finishinitpos"];
    }

    getAction(p: any): String {
        return p["action"];
    }

    getWinner(p: any): String | undefined {
        return p["winner"];
    }

    getInstr(finishchoosecard: boolean, currplayer: String, currworker: number, finishinitpos: boolean, action: String, winner: String | undefined) {
        if (winner === undefined) {
            if (finishchoosecard === false) {
                return "Player " + currplayer + "'s turn:\nChoose god card";
            }
            if (finishinitpos === false) {
                return "Player " + currplayer + "'s turn:\nChoose Worker " + currworker + "\nAction: select starting position";
            }
            return "Player " + currplayer + "'s turn:\nChoose Worker " + currworker + "\nAction: " + action;
        } else {
            return "Player " + winner + " wins!";
        }
    }

    getPlayerCardInfo(playeracard: number, playerbcard: number): String {
        if (playeracard === -1 && playerbcard === -1) {
            return "Player A: Haven't choose play mode.\nPlayer B: Haven't choose play mode.";
        } else if (playeracard === -1) {
            return "Player A: Haven't choose play mode.\nPlayer B: " + powers[playerbcard];
        } else if (playerbcard === -1) {
            return "Player A: " + powers[playeracard] + "\nPlayer B: Haven't choose play mode.";
        } else {
            return "Player A: " + powers[playeracard] + "\nPlayer B: " + powers[playerbcard];
        }
    }

    async newGame() {
        console.log("new game");
        const response = await fetch("newgame");
        const json = await response.json();

        const newCells: Array<Cell> = this.convertToCell(json);
        const newCards: Array<Card> = this.convertToCard(json);
        this.setState({ instructions: "Player A's turn:\nChoose god card", cells: newCells, playerbar: "Player A: Haven't choose play mode.\nPlayer B: Haven't choose play mode.", cards: newCards });
    }

    async chooseGodCard(url: String) {
        console.log("choose god card");
        const href = "choosegodcard?" + url.split("?")[1];
        console.log(href);
        const response = await fetch(href);
        const json = await response.json();
        console.log(json);

        const newCells: Array<Cell> = this.convertToCell(json);
        const newCards: Array<Card> = this.convertToCard(json);
        const finishchoosecard = this.getFinishChooseCard(json);
        const currplayer = this.getCurrPlayer(json);
        const currworker = this.getCurrWorker(json);
        const finishinitpos = this.getFinishInitPos(json);
        const action = this.getAction(json);
        const winner = this.getWinner(json);
        const instr = this.getInstr(finishchoosecard, currplayer, currworker, finishinitpos, action, winner);
        const playeracard = this.getPlayerACard(json);
        const playerbcard = this.getPlayerBCard(json);
        const playerbar = this.getPlayerCardInfo(playeracard, playerbcard);
        this.setState({ instructions: instr, cells: newCells, playerbar: playerbar, cards: newCards });
    }

    async selectWorker(url: String) {
        console.log("select worker");
        const href = "selectworker?" + url.split("?")[1];
        const response = await fetch(href);
        const json = await response.json();

        const newCells: Array<Cell> = this.convertToCell(json);
        const finishchoosecard = this.getFinishChooseCard(json);
        const currplayer = this.getCurrPlayer(json);
        const currworker = this.getCurrWorker(json);
        const finishinitpos = this.getFinishInitPos(json);
        const action = this.getAction(json);
        const winner = this.getWinner(json);
        const instr = this.getInstr(finishchoosecard, currplayer, currworker, finishinitpos, action, winner);
        this.setState({ instructions: instr, cells: newCells });
    }

    async play(url: String) {
        console.log("play");
        const href = "play?" + url.split("?")[1];
        const response = await fetch(href);
        const json = await response.json();

        const newCells: Array<Cell> = this.convertToCell(json);
        const finishchoosecard = this.getFinishChooseCard(json);
        const currplayer = this.getCurrPlayer(json);
        const currworker = this.getCurrWorker(json);
        const finishinitpos = this.getFinishInitPos(json);
        const action = this.getAction(json);
        const winner = this.getWinner(json);
        const instr = this.getInstr(finishchoosecard, currplayer, currworker, finishinitpos, action, winner);
        this.setState({ instructions: instr, cells: newCells });
    }

    async switch() {
        if (
          window.location.href === "http://localhost:3000/newgame" &&
          oldHref !== window.location.href
        ) {
            console.log("switch to new game");
            this.newGame();
            oldHref = window.location.href;
        } else if (
            window.location.href.split("?")[0] === "http://localhost:3000/choosegodcard" &&
            oldHref !== window.location.href
        ) {
            console.log("switch to choose god card");
            this.chooseGodCard(window.location.href);
            oldHref = window.location.href;
        } else if (
            window.location.href.split("?")[0] === "http://localhost:3000/selectworker" &&
            oldHref !== window.location.href
        ) {
            console.log("switch to select worker");
            this.selectWorker(window.location.href);
            oldHref = window.location.href;
        } else if (
            window.location.href.split("?")[0] === "http://localhost:3000/play" &&
            oldHref !== window.location.href
        ) {
            console.log("switch to play");
            this.play(window.location.href);
            oldHref = window.location.href;
        }
    };

    render() {
        this.switch()
        return (
            <div className="App">
                <div
                dangerouslySetInnerHTML={{
                    __html: this.state.template({ instructions: this.state.instructions, cells: this.state.cells,
                        playerbar: this.state.playerbar, cards: this.state.cards }),
                }}
                />
            </div>
        )
    };
};

export default App;