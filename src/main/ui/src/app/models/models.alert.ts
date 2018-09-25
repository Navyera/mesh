export class Alert {
    constructor(public type: AlertType,
                public message: string,
                public sticky: boolean = false,
                public state: string = 'active'
                ) {}
}

export enum AlertType {
    Success,
    Error,
    Info,
    Warning
}

