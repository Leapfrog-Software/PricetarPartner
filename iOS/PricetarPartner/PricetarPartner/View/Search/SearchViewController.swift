//
//  SearchViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class SearchViewController: UIViewController {
    
    @IBOutlet private weak var headerTitleLabel: UILabel!
    @IBOutlet private weak var tableView: UITableView!
    
    private var users = [UserData]()

    func reload() {
        
        guard let myUserData = FetchUserRequester.shared.query(userId: SaveData.shared.userId) else {
            return
        }
        
        self.users.removeAll()
        
        FetchUserRequester.shared.dataList.forEach { user in
            if user.profileType == .none {
                return
            }
            if user.profileType != myUserData.profileType {
                self.users.append(user)
            }
        }
        self.tableView.reloadData()
        
        if myUserData.profileType == .client {
            self.headerTitleLabel.text = "パートナーさんを探す"
        } else {
            self.headerTitleLabel.text = "クライアント様を探す"
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.reload()
    }
}

extension SearchViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.users.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "SearchTableViewCell", for: indexPath) as! SearchTableViewCell
        cell.configure(userData: self.users[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let user = self.users[indexPath.row]
        
        if user.profileType == .client {
            let clientDetail = self.instantiate(storyboard: "Home", identifier: "ClientDetailViewController") as! ClientDetailViewController
            clientDetail.set(user: user)
            self.getTabbarViewController()?.stack(viewController: clientDetail, animationType: .horizontal)
        } else if user.profileType == .partner {
            let partnerDetail = self.instantiate(storyboard: "Home", identifier: "PartnerDetailViewController") as! PartnerDetailViewController
            partnerDetail.set(user: user)
            self.getTabbarViewController()?.stack(viewController: partnerDetail, animationType: .horizontal)
        }
    }
}
