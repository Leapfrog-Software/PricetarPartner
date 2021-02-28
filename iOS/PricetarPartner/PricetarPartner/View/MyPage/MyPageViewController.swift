//
//  MyPageViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class MyPageViewController: UIViewController {

    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var profileTypeLabel: UILabel!
    @IBOutlet private weak var nameLabel: UILabel!
    
    func reload() {
        
        guard let myUserData = FetchUserRequester.shared.query(userId: SaveData.shared.userId) else {
            return
        }
        ImageStorage.shared.fetch(url: UserData.imageUrl(userId: SaveData.shared.userId), imageView: self.userImageView)
        self.profileTypeLabel.text = myUserData.profileType.toText()
        self.nameLabel.text = myUserData.nickname
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.reload()
    }
    
    @IBAction func onTapProfile(_ sender: Any) {
        
        let profile = self.instantiate(storyboard: "MyPage", identifier: "ProfileViewController") as! ProfileViewController
        profile.set(transitionSource: .myPage)
        self.getTabbarViewController()?.stack(viewController: profile, animationType: .horizontal)
    }
}
