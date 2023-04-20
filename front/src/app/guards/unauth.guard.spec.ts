import { UnauthGuard } from './unauth.guard';
import { Router } from '@angular/router';
import { SessionService } from '../services/session.service';

describe('UnAuthGuard', () => {
  let unAuthGuard: UnauthGuard;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(() => {
    router = {
      navigate: jest.fn(),
    } as any;
    sessionService = {
      isLogged: true,
    } as any;
    unAuthGuard = new UnauthGuard(router, sessionService);
  });

  it('Redirecting to /rentals if user is logged in', () => {
    const canActivate = unAuthGuard.canActivate();
    expect(canActivate).toBeFalsy();
    expect(router.navigate).toHaveBeenCalledWith(['rentals']);
  });

  it('canActivate return true if user is not logged in', () => {
    sessionService.isLogged = false;
    const canActivate = unAuthGuard.canActivate();
    expect(canActivate).toBeTruthy();
  });
});
