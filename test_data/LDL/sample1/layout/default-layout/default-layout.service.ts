import { Injectable } from '@angular/core';
import {WebSocketService} from "../../shared/web-socket.service";
import {BehaviorSubject} from "rxjs";
import * as globalParams from "../../global.params";

@Injectable({
  providedIn: 'root'
})
export class DefaultLayoutService {
  private _navigationPortType: {} = {"local_name":"navigationPort"};
  navigationOperationStatus: BehaviorSubject<{}> = new BehaviorSubject<{}>(
    {
      "main": {"notification":"READY"}
    }
  );



  constructor(private webSocketService: WebSocketService){
    this.webSocketService.getSubject().subscribe(
      data => {
        if(data.hasOwnProperty("portType") && data['portType']["local_name"] === this._navigationPortType["local_name"]){
            var stateData = this.navigationOperationStatus.getValue();
            var op = data['operation'];
            if(!(data['operation'] in stateData)){
              stateData[op] = {}
            }
            stateData[op]["notification"] = data['notification'];
            this.navigationOperationStatus.next(stateData);
        }
      }
    );
  }
}
