
export class RegisterInfo {

    constructor(
        public firstName: string,
        public lastName: string,
        public phone: number,
        public picture: File,
        public email: string,
        public password: string,
        public confirmPassword: string
    ) {}
}
