export class Profile {
    constructor(
        public firstName: string = '',
        public lastName: string = '',
        public about: string = '',
        public education: string = '',
        public job: string = '',
        public skills: string[] = null
    ) {}
}
