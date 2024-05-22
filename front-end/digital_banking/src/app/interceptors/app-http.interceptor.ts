import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, throwError} from "rxjs";
import {Injectable} from "@angular/core";
import {AuthService} from "../services/auth.service";

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log("Intercepting request:", request.url);

    if (!request.url.includes("/auth/login")) {
      const authRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.authService.accessToken}`
        }
      });
      return next.handle(authRequest).pipe(
        catchError(err => {
          if (err.status == 401) {
            this.authService.logout();
          }
          return throwError(err.message)
        })
      );
    } else {
      return next.handle(request);
    }
  }
}
