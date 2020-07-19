import { Injectable} from '@angular/core';
import {Observable, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  ws: WebSocket;
  observable: Observable<any>;
  subject: Subject<any>;

  cid: number = null;

  constructor() {
    this.ws = new WebSocket("ws://140.112.90.144:8090/bpel");
    this.observable = this.createObservableSocket();
    this.subject = new Subject<any>();
    this.observable.subscribe(this.subject);
  }

  private createObservableSocket(): Observable<any> {
    // console.log("createObservableSocket");
    return new Observable(
      observer => {
        this.ws.onmessage = (event) => {
          console.log("[WebSocketService] Received:", event.data);
          var jsonObj = JSON.parse(event.data);
          if(jsonObj.hasOwnProperty("notification") && jsonObj["notification"]==="CONNECTIONREADY"){
            this.cid = jsonObj["cid"];
          }else {
            observer.next(jsonObj);
          }
        };
        this.ws.onerror = (event) => {
          console.log("[WebSocketService] Received Error:", event);
          observer.error(event);
        };
        this.ws.onclose = () => {
          observer.complete();
          console.log("[WebSocketService] ws closed");
        };
        this.ws.onopen = () => console.log("[WebSocketService]ws opened");
      }
    );
  }

  getSubject(): Subject<any>{
    return this.subject;
  }

  sendMessage(message: any){
    if(this.cid == null) {
      console.log("[WebSocketService] Socket not connected, wait for 1 sec.");
      setTimeout(() => this.sendMessage(message), 1000);
      return;
    }

    this.ws.send(message);
    console.log("[WebSocketService] Sent:", message);
  }
}
