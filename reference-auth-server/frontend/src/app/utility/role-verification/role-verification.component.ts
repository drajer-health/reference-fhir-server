import { Component, OnInit } from "@angular/core";
import { DataService } from "../../services/data.service";
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
import { Auth0Service } from "../../services/auth0.service";
import { LoaderService } from "../../utility/loader/loader.service";

@Component({
  selector: "app-role-verification",
  templateUrl: "./role-verification.component.html",
  styleUrls: ["./role-verification.component.scss"],
})
export class RoleVerificationComponent implements OnInit {
  constructor(
    private router: Router,
    private localStorage: LocalStorageService,
    public auth: AuthService,
    public auth0Service: Auth0Service,
    public dataService: DataService,
    private _http: HttpClient,
    private loader: LoaderService
  ) {}

  async ngOnInit(): Promise<void> {
    this.auth.getAccessTokenSilently().subscribe({
      next: (token) => {
        this.loader.show();
        this.auth0Service.getAuth0ClientUSerDetails().subscribe({
          next: (data) => {
            this.localStorage.setToStorage("isLogin", "true");
            let temp = {
              userPermissions: data["userRole"],
              email: data["email"],
              name: data["name"],
              userId: data["userId"],
            };
            this.localStorage.setToStorage("userId", data["userId"]);
            this.localStorage.setToStorage("userDetails", JSON.stringify(temp));
            this.dataService.sendUserType(temp);
            this.router.navigate(["/registeredclientapps"]);
            this.loader.hide();
          },
          error: (e) => {},
        });
      },
      error: (err) => {
        this.router.navigate(["home"]);
      },
    });
  }
}
// }
