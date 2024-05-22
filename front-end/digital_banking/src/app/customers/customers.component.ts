import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CustomerService} from "../services/customer.service";
import {catchError, map, Observable, throwError} from "rxjs";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {
  customers!: Observable<Array<Customer>>;
  errorMessage!: string | undefined;
  searchFormGroup!: FormGroup;

  constructor(private customerService: CustomerService, private fb: FormBuilder, private router: Router) {
  }

  ngOnInit(): void {
    // this.http.get("http://localhost:8082/customers").subscribe({
    //   next: (data) => {
    //     this.customers = data;
    //   },
    //   error: (err) => {
    //     console.log(err);
    //   }
    // });
    // -------------------------------
    // this.customerService.getCustomers().subscribe({
    //   next : (data) => {
    //     this.customers = data;
    //   },
    //   error : err => {
    //     this.errorMessage = err.message;
    //   }
    // })
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control("")
    });

    this.handleSearchCustomers();
  }

  handleSearchCustomers() {
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.searchCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }

  handleDeleteCustomer(c: Customer) {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce client ?")) {
      this.customerService.deleteCustomer(c.id).subscribe({
        next: () => {
          this.customers = this.customers.pipe(map(data => {
            let index = data.indexOf(c)
            data.slice(index, 1)
            return data;
          }))
        },
        error: err => {
          console.error("Échec de la suppression du client", err);
          alert("Échec de la suppression du client. Veuillez réessayer plus tard.");
        }
      });
    }
  }

  handleCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl("/customer-accounts/" + customer.id, {state: customer});
  }

}
