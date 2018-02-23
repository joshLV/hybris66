var Ticket = (function () {
    function Ticket(owner, priority, status, shortDescription, id, type, typeDescription, statusDescription, transcript, metadata) {
        this.owner = owner;
        this.priority = priority;
        this.status = status;
        this.shortDescription = shortDescription;
        this.id = id;
        this.type = type;
        this.typeDescription = typeDescription;
        this.statusDescription = statusDescription;
        this.transcript = transcript;
        this.metadata = metadata;
    }
    return Ticket;
}());
export { Ticket };
//# sourceMappingURL=ticket.js.map