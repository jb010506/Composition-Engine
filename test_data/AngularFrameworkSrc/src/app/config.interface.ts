import {Token} from "typedi";

export const portTypeToken = new Token<PortTypeConfig>("porttype");
export const layoutToken = new Token<LayoutConfig>("layout");

export interface PortTypeConfig{
  declarationComponent: any[];
  entryComponent: any[];
  moduleImport: any[];
  routing: any[];
  providers: any[];
}

export interface LayoutConfig{
  declarationComponent: any[];
  entryComponent: any[];
  moduleImport: any[];
  routing: any[];
  providers: any[];
}
