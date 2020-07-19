import {Injectable} from "@angular/core";
import {WebSocketService} from "./shared/web-socket.service";
import {BPEL_WSDL_INFO} from "./config.webapp";
import * as globalParams from "./global.params";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  private _portType = {"local_name":["navigationPort", "startInteractionPort"]};

  constructor(private webSocketService: WebSocketService) {
    this.webSocketService.getSubject().subscribe(
      data => {
        if(data.hasOwnProperty("portType") && this._portType["local_name"].indexOf(data['portType']["local_name"]) > -1 ){
          if(data.hasOwnProperty("notification") && data["notification"] === "READY") {
            globalParams.routingInvoker[data["operation"]] = data;
          }
        }
      }
    );
    // start main bpel
    this.sendMessage(JSON.stringify(BPEL_WSDL_INFO));
  }

  sendMessage(message: string){
      this.webSocketService.sendMessage(message);
  }

}
