export class Settings {
    constructor(public firstName = '',
                public lastName = '',
                public phone = '',
                public picture = null,
                public email = '',
                public oldPassword = '',
                public newPassword = '',
                public confirmNewPassword = ''
                ) { }
}
