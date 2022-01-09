import { BoatOwnerService } from './../../../service/boat-owner.service';
import { BoatService } from 'src/app/service/boat.service';
import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { AuthService, UserService } from '../../../service';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

interface DisplayMessage {
  msgType: string;
  msgBody: string;
}
@Component({
  selector: 'app-my-boats',
  templateUrl: './my-boats.component.html',
  styleUrls: ['./my-boats.component.css']
})
export class MyBoatsComponent implements OnInit {
  returnUrl: string;
  notification: DisplayMessage; 
  private ngUnsubscribe: Subject<void> = new Subject<void>();
  boats: any 
  form: FormGroup;
  searchDTO = {
    "name" : "",
    "address" : "",
    "rating" : "",
   // "price" : "",
    "description" : ""
  }
  constructor(private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private boatService: BoatService,
    private boatOwnerService: BoatOwnerService,
    private formBuilder: FormBuilder
    ) { }

  ngOnInit() {
    this.form = this.formBuilder.group({  
      name: [''],
      address: [''], 
      description: [''],  
      rating: ['',Validators.compose([Validators.min(0), Validators.pattern('([0-9]+\.?[0-9]*|\.[0-9]+)$')])]  
    })  

   this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
   this.boatOwnerService.getBoatsFromOwner().subscribe((data : any) => {
     this.boats = data;
   });

   console.log(this.boats);
  }

  search(){
    this.searchDTO.name = this.form.get('name').value
    this.searchDTO.address = this.form.get('address').value
    this.searchDTO.rating = this.form.get('rating').value
    //this.searchDTO.price = this.form.get('price').value
    this.searchDTO.description = this.form.get('description').value 
    this.boatService.search(this.searchDTO).subscribe((data : any) => { 
      this.boats = data; 
    }); 
  }

  clear(){
    this.form.setValue({"name" : "", "address" : "", "rating": "", "description" : ""})
    this.boatService.getAll().subscribe((data : any) => {
      this.boats = data;
    }); 
  }

}
