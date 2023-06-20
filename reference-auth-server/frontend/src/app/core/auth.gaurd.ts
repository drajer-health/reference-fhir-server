import { DataService } from "./../services/data.service";
import { AuthService } from "@auth0/auth0-angular";
import { LocalStorageService } from "src/app/core/localstorage.service";
import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { authurl, base_url } from "src/environments/environment";
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from "@angular/router";
import { Auth0Service } from "../services/auth0.service";
import { LoaderService } from "../utility/loader/loader.service";

@Injectable()
export class CustomAuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private localStorage: LocalStorageService,
    public auth: AuthService,
    public auth0Service: Auth0Service,
    public dataService: DataService,
    private _http: HttpClient,
    private loader: LoaderService
  ) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    // this.auth.getAccessTokenSilently().subscribe({
    //   next: () => {
    //     this.loader.show();
    //     console.log("getAccessTokenSilently ");
    //     this.auth0Service.getAuth0ClientUSerDetails().subscribe({
    //       next: (data) => {
    //         this.localStorage.setToStorage("isLogin", "true");
    //         let temp = {
    //           userPermissions: data["userRole"],
    //           email: data["email"],
    //           name: data["name"],
    //           userId: data["userId"],
    //         };
    //         this.localStorage.setToStorage("userId", data["userId"]);
    //         this.localStorage.setToStorage("userDetails", JSON.stringify(temp));
    //         this.dataService.sendUserType(temp);
    //         this.router.navigate(["/registeredclientapps"]);
    //         this.loader.hide();
    //       },
    //       error: (e) => {
    //         debugger;
    //       },
    //     });
    //   },
    // });

    return true;
  }
}
