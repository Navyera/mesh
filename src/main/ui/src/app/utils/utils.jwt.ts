export class JWTToken {
    private data;

    constructor(private token: string) {
        this.data = JSON.parse(window.atob(token.split(' ')[1].split('.')[1]));
        // console.log(this.data);
    }

    getClaim(key: string): string {
        return this.data[key];
    }

    isExpired(): boolean {
        // console.log(new Date().getTime() / 1000);
        // console.log(this.data.exp);
        return (new Date().getTime() / 1000) > this.data.exp;
    }
}
