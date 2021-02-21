//
//  RegisterUserViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class RegisterUserViewController: UIViewController {

    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var passwordTextField: UITextField!
    
    @IBAction func didEndOnExitTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        
        self.view.endEditing(true)
        self.pop(animationType: .horizontal)
    }

    @IBAction func onTapRegister(_ sender: Any) {

        self.view.endEditing(true)
        
        let email = self.emailTextField.text ?? ""
        let password = self.passwordTextField.text ?? ""
        
        
        
        RegisterUserRequester.register(email: email, password: password, completion: { result, userId in

            if result, let userId = userId {
                
            }
        })
    }
}
