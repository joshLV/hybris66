import {Injectable} from '@angular/core';


@Injectable()
export class TicketUtils {

  constructor () {}

  public getCurrentLanguage() {
   return $('select[id=lang-selector]').val();
  }
}
