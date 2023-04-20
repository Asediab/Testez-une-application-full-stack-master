import { AuthGuard } from './auth.guard';
import { Router } from '@angular/router';
import { SessionService } from '../services/session.service';

describe('AuthGuard', () => {
  let authGuard: AuthGuard;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(() => {
    router = {
      navigate: jest.fn(),
    } as any;
    sessionService = {
      isLogged: true,
    } as any;
    authGuard = new AuthGuard(router, sessionService);
  });

  it('Redirecting to /login if user is logged in', () => {
    sessionService.isLogged = false;
    const canActivate = authGuard.canActivate();
    expect(canActivate).toBeFalsy();
    expect(router.navigate).toHaveBeenCalledWith(['login']);
  });

  it('canActivate return true if user is logged in', () => {
    const canActivate = authGuard.canActivate();
    expect(canActivate).toBeTruthy();
  });
});
