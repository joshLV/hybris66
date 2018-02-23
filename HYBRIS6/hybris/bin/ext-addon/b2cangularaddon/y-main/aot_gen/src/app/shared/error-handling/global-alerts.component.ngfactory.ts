/**
 * @fileoverview This file is generated by the Angular template compiler.
 * Do not edit.
 * @suppress {suspiciousCode,uselessCode,missingProperties,missingOverride}
 */
 /* tslint:disable */


import * as import0 from '@angular/core';
import * as import1 from '@angular/common';
import * as import2 from '../../../../../src/app/shared/error-handling/global-alerts.component';
const styles_GlobalAlertsComponent:any[] = ([] as any[]);
export const RenderType_GlobalAlertsComponent:import0.RendererType2 = import0.ɵcrt({
  encapsulation: 2,
  styles: styles_GlobalAlertsComponent,
  data: {}
}
);
function View_GlobalAlertsComponent_2(l:any):import0.ɵViewDefinition {
  return import0.ɵvid(0,[
      (l()(),import0.ɵeld(0,(null as any),(null as any),4,'div',[[
        'class',
        'alert alert-info'
      ]
    ],(null as any),(null as any),(null as any),(null as any),(null as any))),
    (l()(),import0.ɵted((null as any),['\n    '])),
    (l()(),import0.ɵeld(0,(null as any),(null as any),1,'button',[
      [
        'class',
        'close'
      ]
      ,
      [
        'type',
        'button'
      ]

    ]
      ,(null as any),[[
        (null as any),
        'click'
      ]
    ],(v,en,$event) => {
      var ad:boolean = true;
      var co:any = v.component;
      if (('click' === en)) {
        const pd_0:any = ((<any>co.clear('conf')) !== false);
        ad = (pd_0 && ad);
      }
      return ad;
    },(null as any),(null as any))),
    (l()(),import0.ɵted((null as any),['×'])),
    (l()(),import0.ɵted((null as any),[
      '\n    ',
      '\n  '
    ]
    ))
  ]
  ,(null as any),(ck,v) => {
    const currVal_0:any = v.context.$implicit;
    ck(v,4,0,currVal_0);
  });
}
function View_GlobalAlertsComponent_3(l:any):import0.ɵViewDefinition {
  return import0.ɵvid(0,[
      (l()(),import0.ɵeld(0,(null as any),(null as any),4,'div',[[
        'class',
        'alert alert-warning'
      ]
    ],(null as any),(null as any),(null as any),(null as any),(null as any))),
    (l()(),import0.ɵted((null as any),['\n    '])),
    (l()(),import0.ɵeld(0,(null as any),(null as any),1,'button',[
      [
        'class',
        'close'
      ]
      ,
      [
        'type',
        'button'
      ]

    ]
      ,(null as any),[[
        (null as any),
        'click'
      ]
    ],(v,en,$event) => {
      var ad:boolean = true;
      var co:any = v.component;
      if (('click' === en)) {
        const pd_0:any = ((<any>co.clear('info')) !== false);
        ad = (pd_0 && ad);
      }
      return ad;
    },(null as any),(null as any))),
    (l()(),import0.ɵted((null as any),['×'])),
    (l()(),import0.ɵted((null as any),[
      '\n    ',
      '\n  '
    ]
    ))
  ]
  ,(null as any),(ck,v) => {
    const currVal_0:any = v.context.$implicit;
    ck(v,4,0,currVal_0);
  });
}
function View_GlobalAlertsComponent_4(l:any):import0.ɵViewDefinition {
  return import0.ɵvid(0,[
      (l()(),import0.ɵeld(0,(null as any),(null as any),4,'div',[[
        'class',
        'alert alert-danger'
      ]
    ],(null as any),(null as any),(null as any),(null as any),(null as any))),
    (l()(),import0.ɵted((null as any),['\n    '])),
    (l()(),import0.ɵeld(0,(null as any),(null as any),1,'button',[
      [
        'class',
        'close'
      ]
      ,
      [
        'type',
        'button'
      ]

    ]
      ,(null as any),[[
        (null as any),
        'click'
      ]
    ],(v,en,$event) => {
      var ad:boolean = true;
      var co:any = v.component;
      if (('click' === en)) {
        const pd_0:any = ((<any>co.clear('error')) !== false);
        ad = (pd_0 && ad);
      }
      return ad;
    },(null as any),(null as any))),
    (l()(),import0.ɵted((null as any),['×'])),
    (l()(),import0.ɵted((null as any),[
      '\n    ',
      '\n  '
    ]
    ))
  ]
  ,(null as any),(ck,v) => {
    const currVal_0:any = v.context.$implicit;
    ck(v,4,0,currVal_0);
  });
}
function View_GlobalAlertsComponent_1(l:any):import0.ɵViewDefinition {
  return import0.ɵvid(0,[
      (l()(),import0.ɵeld(0,(null as any),(null as any),10,'div',[[
        'class',
        'global-alerts'
      ]
    ],(null as any),(null as any),(null as any),(null as any),(null as any))),
    (l()(),import0.ɵted((null as any),['\n  '])),
    (l()(),import0.ɵand(16777216,(null as any),(null as any),1,(null as any),View_GlobalAlertsComponent_2)),
    import0.ɵdid(802816,(null as any),0,import1.NgForOf,[
      import0.ViewContainerRef,
      import0.TemplateRef,
      import0.IterableDiffers
    ]
      ,{ngForOf: [
        0,
        'ngForOf'
      ]
    },(null as any)),
    (l()(),import0.ɵted((null as any),['\n  '])),
    (l()(),import0.ɵand(16777216,(null as any),(null as any),1,(null as any),View_GlobalAlertsComponent_3)),
    import0.ɵdid(802816,(null as any),0,import1.NgForOf,[
      import0.ViewContainerRef,
      import0.TemplateRef,
      import0.IterableDiffers
    ]
      ,{ngForOf: [
        0,
        'ngForOf'
      ]
    },(null as any)),
    (l()(),import0.ɵted((null as any),['\n  '])),
    (l()(),import0.ɵand(16777216,(null as any),(null as any),1,(null as any),View_GlobalAlertsComponent_4)),
    import0.ɵdid(802816,(null as any),0,import1.NgForOf,[
      import0.ViewContainerRef,
      import0.TemplateRef,
      import0.IterableDiffers
    ]
      ,{ngForOf: [
        0,
        'ngForOf'
      ]
    },(null as any)),
    (l()(),import0.ɵted((null as any),['\n']))
  ]
  ,(ck,v) => {
    var co:any = v.component;
    const currVal_0:any = co.confMsgs;
    ck(v,3,0,currVal_0);
    const currVal_1:any = co.infoMsgs;
    ck(v,6,0,currVal_1);
    const currVal_2:any = co.errorMsgs;
    ck(v,9,0,currVal_2);
  },(null as any));
}
export function View_GlobalAlertsComponent_0(l:any):import0.ɵViewDefinition {
  return import0.ɵvid(0,[
    (l()(),import0.ɵand(16777216,(null as any),(null as any),1,(null as any),View_GlobalAlertsComponent_1)),
    import0.ɵdid(16384,(null as any),0,import1.NgIf,[
      import0.ViewContainerRef,
      import0.TemplateRef
    ]
      ,{ngIf: [
        0,
        'ngIf'
      ]
    },(null as any)),
    (l()(),import0.ɵted((null as any),['\n']))
  ]
  ,(ck,v) => {
    var co:import2.GlobalAlertsComponent = v.component;
    const currVal_0:any = ((co.hasConfMsgs && co.hasInfoMsgs) && co.hasErrorMsgs);
    ck(v,1,0,currVal_0);
  },(null as any));
}
function View_GlobalAlertsComponent_Host_0(l:any):import0.ɵViewDefinition {
  return import0.ɵvid(0,[
    (l()(),import0.ɵeld(0,(null as any),(null as any),1,'global-alerts',([] as any[]),(null as any),(null as any),(null as any),View_GlobalAlertsComponent_0,RenderType_GlobalAlertsComponent)),
    import0.ɵdid(49152,(null as any),0,import2.GlobalAlertsComponent,([] as any[]),(null as any),(null as any))
  ]
  ,(null as any),(null as any));
}
export const GlobalAlertsComponentNgFactory:import0.ComponentFactory<import2.GlobalAlertsComponent> = import0.ɵccf('global-alerts',import2.GlobalAlertsComponent,View_GlobalAlertsComponent_Host_0,{
  confMsgs: 'confMsgs',
  infoMsgs: 'infoMsgs',
  errorMsgs: 'errorMsgs'
}
,{onCleared: 'onCleared'},([] as any[]));
//# sourceMappingURL=data:application/json;base64,eyJmaWxlIjoiL3Nydi9qZW5raW5zL3dvcmtzcGFjZS8wMi1wYWNrYWdlL3NvdXJjZS9iMmNhbmd1bGFyYWRkb24veS1tYWluL3NyYy9hcHAvc2hhcmVkL2Vycm9yLWhhbmRsaW5nL2dsb2JhbC1hbGVydHMuY29tcG9uZW50Lm5nZmFjdG9yeS50cyIsInZlcnNpb24iOjMsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIm5nOi8vL3Nydi9qZW5raW5zL3dvcmtzcGFjZS8wMi1wYWNrYWdlL3NvdXJjZS9iMmNhbmd1bGFyYWRkb24veS1tYWluL3NyYy9hcHAvc2hhcmVkL2Vycm9yLWhhbmRsaW5nL2dsb2JhbC1hbGVydHMuY29tcG9uZW50LnRzIiwibmc6Ly8vc3J2L2plbmtpbnMvd29ya3NwYWNlLzAyLXBhY2thZ2Uvc291cmNlL2IyY2FuZ3VsYXJhZGRvbi95LW1haW4vc3JjL2FwcC9zaGFyZWQvZXJyb3ItaGFuZGxpbmcvZ2xvYmFsLWFsZXJ0cy5jb21wb25lbnQuaHRtbCIsIm5nOi8vL3Nydi9qZW5raW5zL3dvcmtzcGFjZS8wMi1wYWNrYWdlL3NvdXJjZS9iMmNhbmd1bGFyYWRkb24veS1tYWluL3NyYy9hcHAvc2hhcmVkL2Vycm9yLWhhbmRsaW5nL2dsb2JhbC1hbGVydHMuY29tcG9uZW50LnRzLkdsb2JhbEFsZXJ0c0NvbXBvbmVudF9Ib3N0Lmh0bWwiXSwic291cmNlc0NvbnRlbnQiOlsiICIsIjxkaXYgY2xhc3M9XCJnbG9iYWwtYWxlcnRzXCIgKm5nSWY9XCJoYXNDb25mTXNncyAmJiBoYXNJbmZvTXNncyAmJiBoYXNFcnJvck1zZ3NcIj5cbiAgPGRpdiBjbGFzcz1cImFsZXJ0IGFsZXJ0LWluZm9cIiAqbmdGb3I9XCJsZXQgY29uZk1zZyBvZiBjb25mTXNnc1wiPlxuICAgIDxidXR0b24gY2xhc3M9XCJjbG9zZVwiIHR5cGU9XCJidXR0b25cIiAoY2xpY2spPVwiY2xlYXIoJ2NvbmYnKVwiPsOXPC9idXR0b24+XG4gICAge3tjb25mTXNnfX1cbiAgPC9kaXY+XG4gIDxkaXYgY2xhc3M9XCJhbGVydCBhbGVydC13YXJuaW5nXCIgKm5nRm9yPVwibGV0IGluZm9Nc2cgb2YgaW5mb01zZ3NcIj5cbiAgICA8YnV0dG9uIGNsYXNzPVwiY2xvc2VcIiB0eXBlPVwiYnV0dG9uXCIgKGNsaWNrKT1cImNsZWFyKCdpbmZvJylcIj7DlzwvYnV0dG9uPlxuICAgIHt7aW5mb01zZ319XG4gIDwvZGl2PlxuICA8ZGl2IGNsYXNzPVwiYWxlcnQgYWxlcnQtZGFuZ2VyXCIgKm5nRm9yPVwibGV0IGVycm9yTXNnIG9mIGVycm9yTXNnc1wiPlxuICAgIDxidXR0b24gY2xhc3M9XCJjbG9zZVwiIHR5cGU9XCJidXR0b25cIiAoY2xpY2spPVwiY2xlYXIoJ2Vycm9yJylcIj7DlzwvYnV0dG9uPlxuICAgIHt7ZXJyb3JNc2d9fVxuICA8L2Rpdj5cbjwvZGl2PlxuIiwiPGdsb2JhbC1hbGVydHM+PC9nbG9iYWwtYWxlcnRzPiJdLCJtYXBwaW5ncyI6IkFBQUE7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7O01DQ0U7UUFBQTtRQUFBO01BQUE7SUFBQTtJQUErRDtJQUM3RDtNQUFBO1FBQUE7UUFBQTtNQUFBOztNQUFBO1FBQUE7UUFBQTtNQUFBOztJQUFBO09BQUE7UUFBQTtRQUFBO01BQUE7SUFBQTtNQUFBO01BQUE7TUFBb0M7UUFBQTtRQUFBO01BQUE7TUFBcEM7SUFBQTtJQUE0RDtJQUFVO01BQUE7TUFBQTtJQUFBO0lBQUE7OztJQUFBO0lBQUE7Ozs7O01BR3hFO1FBQUE7UUFBQTtNQUFBO0lBQUE7SUFBa0U7SUFDaEU7TUFBQTtRQUFBO1FBQUE7TUFBQTs7TUFBQTtRQUFBO1FBQUE7TUFBQTs7SUFBQTtPQUFBO1FBQUE7UUFBQTtNQUFBO0lBQUE7TUFBQTtNQUFBO01BQW9DO1FBQUE7UUFBQTtNQUFBO01BQXBDO0lBQUE7SUFBNEQ7SUFBVTtNQUFBO01BQUE7SUFBQTtJQUFBOzs7SUFBQTtJQUFBOzs7OztNQUd4RTtRQUFBO1FBQUE7TUFBQTtJQUFBO0lBQW1FO0lBQ2pFO01BQUE7UUFBQTtRQUFBO01BQUE7O01BQUE7UUFBQTtRQUFBO01BQUE7O0lBQUE7T0FBQTtRQUFBO1FBQUE7TUFBQTtJQUFBO01BQUE7TUFBQTtNQUFvQztRQUFBO1FBQUE7TUFBQTtNQUFwQztJQUFBO0lBQTZEO0lBQVU7TUFBQTtNQUFBO0lBQUE7SUFBQTs7O0lBQUE7SUFBQTs7Ozs7TUFWM0U7UUFBQTtRQUFBO01BQUE7SUFBQTtJQUE4RTtJQUM1RTtnQkFBQTs7OztJQUFBO09BQUE7UUFBQTtRQUFBO01BQUE7SUFBQTtJQUdNO0lBQ047Z0JBQUE7Ozs7SUFBQTtPQUFBO1FBQUE7UUFBQTtNQUFBO0lBQUE7SUFHTTtJQUNOO2dCQUFBOzs7O0lBQUE7T0FBQTtRQUFBO1FBQUE7TUFBQTtJQUFBO0lBR007Ozs7SUFYd0I7SUFBOUIsU0FBOEIsU0FBOUI7SUFJaUM7SUFBakMsU0FBaUMsU0FBakM7SUFJZ0M7SUFBaEMsU0FBZ0MsU0FBaEM7Ozs7O0lBVEY7Z0JBQUE7OztJQUFBO09BQUE7UUFBQTtRQUFBO01BQUE7SUFBQTtJQWFNOzs7O0lBYnFCO0lBQTNCLFNBQTJCLFNBQTNCOzs7OztJQ0FBO2dCQUFBOzs7Ozs7Ozs7In0=