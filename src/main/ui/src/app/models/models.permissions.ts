export class Permissions {
    private readonly ABOUT: number     = (1 << 0);
    private readonly EDUCATION: number = (1 << 1);
    private readonly JOB: number       = (1 << 2);
    private readonly SKILLS: number    = (1 << 3);
    private readonly FRIEND: number    = (1 << 4);

    constructor(private permissions: number = 0) {}

    public assign(permissions: Permissions) {
        this.permissions = permissions.permissions;
    }

    public getAbout(): boolean {
        return (this.permissions & this.ABOUT) !== 0;
    }

    public getEducation(): boolean {
        return (this.permissions & this.EDUCATION) !== 0;
    }

    public getJob(): boolean {
        return (this.permissions & this.JOB) !== 0;
    }

    public getSkills(): boolean {
        return (this.permissions & this.SKILLS) !== 0;
    }

    public getFriend(): boolean {
        return (this.permissions & this.FRIEND) !== 0;
    }

    public setAbout(value: boolean): void  {
        this.permissions = value ? this.permissions | this.ABOUT : this.permissions & ~this.ABOUT;
    }

    public setEducation(value: boolean): void   {
        this.permissions = value ? this.permissions | this.EDUCATION : this.permissions & ~this.EDUCATION;
    }

    public setJob(value: boolean): void   {
        this.permissions = value ? this.permissions | this.JOB : this.permissions & ~this.JOB;
    }

    public setSkills(value: boolean): void   {
        this.permissions = value ? this.permissions | this.SKILLS : this.permissions & ~this.SKILLS;
    }

    public setFriend(value: boolean): void   {
        this.permissions = value ? this.permissions | this.FRIEND : this.permissions & ~this.FRIEND;
    }
}

export class PermissionsManager {
    private permissions: Permissions;

    constructor(permissions: Permissions) {
        this.changePermissonsObj(permissions);
    }

    private _about = false;
    private _education = false;
    private _job = false;
    private _skills = false;
    private _friends = false;

    changePermissonsObj(permissions: Permissions) {
        this.permissions = permissions;

        this._about = permissions.getAbout();
        this._education = permissions.getEducation();
        this._job = permissions.getJob();
        this._skills = permissions.getSkills();
        this._friends = permissions.getFriend();
    }

    get about(): boolean {
        return this._about;
    }

    set about(value: boolean) {
        this._about = value;

        this.permissions.setAbout(value);
    }

    get education(): boolean {
        return this._education;
    }

    set education(value: boolean) {
        this._education = value;

        this.permissions.setEducation(value);
    }

    get job(): boolean {
        return this._job;
    }

    set job(value: boolean) {
        this._job = value;

        this.permissions.setJob(value);
    }

    get skills(): boolean {
        return this._skills;
    }

    set skills(value: boolean) {
        this._skills = value;

        this.permissions.setSkills(value);
    }

    get friends(): boolean {
        return this._friends;
    }

    set friends(value: boolean) {
        this._friends = value;

        this.permissions.setFriend(value);
    }
}
