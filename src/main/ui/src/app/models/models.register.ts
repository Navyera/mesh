
export class RegisterInfo {
    constructor(
        public firstName: string = '',
        public lastName: string = '',
        public phone: string = '',
        public email: string = '',
        public password: string = '',
        public confirmPassword: string = ''
    ) {}
}
