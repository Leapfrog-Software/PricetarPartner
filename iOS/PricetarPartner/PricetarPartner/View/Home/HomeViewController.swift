//
//  HomeViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class HomeViewController: UIViewController {
    
    @IBOutlet private weak var tableView: UITableView!

    private var newUsers = [UserData]()
    private var popularUsers = [UserData]()
    
    func reload() {
        
        guard let myUserData = FetchUserRequester.shared.query(userId: SaveData.shared.userId) else {
            return
        }
        
        self.newUsers.removeAll()
        self.popularUsers.removeAll()
        
        let filterredUsers = FetchUserRequester.shared.dataList.filter { user -> Bool in
            if user.profileType != .none {
                return user.profileType != myUserData.profileType
            }
            return false
        }
        
        let sortedUsersByNew = filterredUsers.sorted(by: { user1, user2 -> Bool in
            // TODO
            return true
        })
        for i in 0..<4 {
            if sortedUsersByNew.count > i {
                self.newUsers.append(sortedUsersByNew[i])
            }
        }
        
        let sortedUsersByPopular = filterredUsers.sorted(by: { user1, user2 -> Bool in
            // TODO
            return true
        })
        for i in 0..<4 {
            if sortedUsersByPopular.count > i {
                self.popularUsers.append(sortedUsersByPopular[i])
            }
        }
        
        self.tableView.reloadData()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.reload()
    }
    

}

extension HomeViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 4
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        // 新着タイトル
        if section == 0 {
            return 1
        }
        // 新着ユーザー
        else if section == 1 {
            return self.newUsers.count
        }
        // 人気タイトル
        else if section == 2 {
            return 1
        }
        // 人気ユーザー
        else {
            return self.popularUsers.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // 新着タイトル
        if indexPath.section == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeNewTitleTableViewCell", for: indexPath) as! HomeNewTitleTableViewCell
            return cell
        }
        // 新着ユーザー
        else if indexPath.section == 1 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeNewUserTableViewCell", for: indexPath) as! HomeNewUserTableViewCell
            cell.configure(user: self.newUsers[indexPath.row])
            return cell
        }
        // 人気タイトル
        else if indexPath.section == 2 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomePopularTitleTableViewCell", for: indexPath) as! HomePopularTitleTableViewCell
            return cell
        }
        // 人気ユーザー
        else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomePopularUserTableViewCell", for: indexPath) as! HomePopularUserTableViewCell
            cell.configure(user: self.popularUsers[indexPath.row])
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        // 新着ユーザー
        if indexPath.section == 1 {
            self.stackUserDetail(user: self.newUsers[indexPath.row])
        }
        // 人気ユーザー
        else if indexPath.section == 3 {
            self.stackUserDetail(user: self.popularUsers[indexPath.row])
        }
    }
    
    private func stackUserDetail(user: UserData) {
        
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
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        // 新着タイトル
        if indexPath.section == 0 {
            return 60
        }
        // 新着ユーザー
        else if indexPath.section == 1 {
            return 120
        }
        // 人気タイトル
        else if indexPath.section == 2 {
            return 60
        }
        // 人気ユーザー
        else {
            return 120
        }
    }
}
