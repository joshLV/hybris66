import {MetaData} from "./metadata";
import {TicketType} from "./ticketType";
import {Transcript} from "./transcript";

export class Ticket {
  constructor(
    public owner: string,
    public priority: string,
    public status: string,
    public shortDescription: string,
    public id: string,
    public type: String,
    public typeDescription: string[],
    public statusDescription: string[],
    public transcript:Transcript[],
    public metadata:MetaData) { }

}
