import { Profile } from './models.profile';
import { Permissions } from './models.permissions';

export class ProfileView {
    constructor(public profileDTO: Profile,
                public permissionsDTO: Permissions) {}

}
