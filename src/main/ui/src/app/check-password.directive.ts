import { Directive } from '@angular/core';
import { AbstractControl, FormGroup, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn } from '@angular/forms';

function validatePassword(): ValidatorFn {
  return (control: AbstractControl) => {
    let isValid = false;

    if (control && control instanceof FormGroup) {
      const group = control as FormGroup;

      try {
        isValid = group.controls['password'].value === group.controls['confirmPassword'].value;
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
export class CheckPasswordDirective {
  private valFn;

  constructor() {
    this.valFn = validatePassword();
  }

  validate(c: AbstractControl): ValidationErrors | null {
    return this.valFn(c);
  }
}
