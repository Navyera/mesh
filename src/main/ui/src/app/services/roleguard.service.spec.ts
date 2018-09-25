import { TestBed, inject } from '@angular/core/testing';

import { RoleguardService } from './roleguard.service';

describe('RoleguardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RoleguardService]
    });
  });

  it('should be created', inject([RoleguardService], (service: RoleguardService) => {
    expect(service).toBeTruthy();
  }));
});
