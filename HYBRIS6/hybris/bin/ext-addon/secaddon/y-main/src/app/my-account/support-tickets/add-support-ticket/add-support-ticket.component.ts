import {Component} from '@angular/core';
import {SupportTicketsService} from '../service/support-tickets.service';
import {TicketType} from '../models/ticketType';
import {Ticket} from '../models/ticket';
import {Router} from '@angular/router';
import {ErrorData} from "../models/errordata";

@Component({
  selector: 'y-main',
  templateUrl: './add-support-ticket.component.html',
  styleUrls:['./add-support-ticket.component.css'],
  providers: [SupportTicketsService]
})

export class AddSupportTicketComponent {

  ticket = new Ticket('','MEDIUM', 'OPEN', null, null, null, null, null,null,null);
  ticketTypes: TicketType[];
  isCreateInProgress: boolean = false;
  error:ErrorData;

  constructor(private supportTicketsService: SupportTicketsService, private router: Router) {}

  ngOnInit(): void {
    this.supportTicketsService.getTicketTypes().subscribe(data => {
      this.ticketTypes = data;
      this.ticket.type = this.ticketTypes[0].type;
    });
  }

  public createTicket() {
    if (this.canCreateTicket()) {
      this.isCreateInProgress = true;
      this.supportTicketsService.createTicket(this.ticket)
        .subscribe(() => {this.router.navigate(['/my-account/support-tickets']);this.isCreateInProgress = false;}, (err:ErrorData) => {this.showError(err);});
    }
  }

  private showError(error:ErrorData){
    this.error = error;
  }

  public canCreateTicket(): boolean {
    return !this.isBlank(this.ticket.shortDescription) && !this.isCreateInProgress;
  }

  private isBlank(str:string):boolean {
    return (!str || /^\s*$/.test(str));
  }

  public goBack() {
    this.router.navigate(['/my-account/support-tickets']);
  }

}
