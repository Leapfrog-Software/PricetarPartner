//
//  ClientDetailViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/26.
//

import UIKit

class ClientDetailViewController: UIViewController {

    private var user: UserData!
    
    func set(user: UserData) {
        self.user = user
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

    }
    

    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}
