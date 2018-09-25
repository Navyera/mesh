import { Directive, Input } from '@angular/core';
import { AbstractControl, FormGroup, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn } from '@angular/forms';

function validatePassword(password: string, confirmPassword: string): ValidatorFn {
  return (control: AbstractControl) => {
    let isValid = false;

    if (control && control instanceof FormGroup) {
      const group = control as FormGroup;

      try {
        isValid = group.controls[password].value === group.controls[confirmPassword].value;
      } catch {
        isValid = false;
      }

      if (isValid) {
        return null;
      } else {
        return { 'passwordCheck': 'failed' };
      }
    }
  };
}

@Directive({
  selector: '[appCheckPassword]',
  providers: [{ provide: NG_VALIDATORS, useExisting: CheckPasswordDirective, multi: true }]
})
export class CheckPasswordDirective implements Validator {
  @Input('password') password: string;
  @Input('confirmPassword') confirmPassword: string;

  constructor() {}

  validate(c: AbstractControl): ValidationErrors | null {
    return this.password && this.confirmPassword ? validatePassword(this.password, this.confirmPassword)(c) : null;
  }
}
