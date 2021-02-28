//
//  ProfileViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class ProfileViewController: UIViewController {
    
    enum TransitionSource {
        case splash
        case register
        case myPage
    }
    
    @IBOutlet private weak var backButton: UIButton!
    @IBOutlet private weak var clientButton: UIButton!
    @IBOutlet private weak var partnerButton: UIButton!
    @IBOutlet private weak var containerView: UIView!
    
    private var profileClientViewController: ProfileClientViewController!
    private var profilePartnerViewController: ProfilePartnerViewController!
    private var transitionSource: TransitionSource!
    
    func set(transitionSource: TransitionSource) {
        self.transitionSource = transitionSource
    }
    
    func didUpdate() {
        
        // スプラッシュから遷移(ログイン後プロフィール未設定)
        if self.transitionSource == .splash {
            if let splash = self.parent as? SplashViewController {
                splash.fetch()
            }
        }
        // 新規登録画面から遷移(新規登録済みプロフィール未設定)
        else if self.transitionSource == .register {
            if let splash = self.parent?.parent as? SplashViewController {
                splash.fetch()
            }
        }
        // マイページから遷移
        else {
            self.getTabbarViewController()?.reload()
            
            let action = DialogAction(title: "OK", action: { [weak self] in
                self?.pop(animationType: .horizontal)
            })
            Dialog.show(style: .success, title: "確認", message: "更新しました", actions: [action])
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let profileClientViewController = self.instantiate(storyboard: "MyPage", identifier: "ProfileClientViewController") as! ProfileClientViewController
        self.addViewController(viewController: profileClientViewController)
        self.profileClientViewController = profileClientViewController
        
        let profilePartnerViewController = self.instantiate(storyboard: "MyPage", identifier: "ProfilePartnerViewController") as! ProfilePartnerViewController
        self.addViewController(viewController: profilePartnerViewController)
        self.profilePartnerViewController = profilePartnerViewController
        
        if FetchUserRequester.shared.query(userId: SaveData.shared.userId)?.profileType == UserData.ProfileType.partner {
            self.changeSegment(index: 1)
        } else {
            self.changeSegment(index: 0)
        }
        
        switch self.transitionSource {
        case .splash:
            self.backButton.isHidden = false
        case .register:
            self.backButton.isHidden = true
        case .myPage:
            self.backButton.isHidden = false
        default:
            break
        }
    }
    
    private func addViewController(viewController: UIViewController) {

        self.containerView.addSubview(viewController.view)
        self.addChild(viewController)
        viewController.didMove(toParent: self)
        
        viewController.view.translatesAutoresizingMaskIntoConstraints = false
        viewController.view.topAnchor.constraint(equalTo: self.containerView.topAnchor).isActive = true
        viewController.view.leadingAnchor.constraint(equalTo: self.containerView.leadingAnchor).isActive = true
        viewController.view.trailingAnchor.constraint(equalTo: self.containerView.trailingAnchor).isActive = true
        viewController.view.bottomAnchor.constraint(equalTo: self.containerView.bottomAnchor).isActive = true
    }
    
    private func changeSegment(index: Int) {
        
        self.clientButton.backgroundColor = (index == 0) ? .mainOrange : UIColor(white: 0.9, alpha: 1.0)
        self.partnerButton.backgroundColor = (index == 1) ? .mainOrange : UIColor(white: 0.9, alpha: 1.0)
        
        self.clientButton.setTitleColor((index == 0) ? .white : UIColor(white: 0.6, alpha: 1.0), for: .normal)
        self.partnerButton.setTitleColor((index == 1) ? .white : UIColor(white: 0.6, alpha: 1.0), for: .normal)
        
        self.profileClientViewController.view.isHidden = (index != 0)
        self.profilePartnerViewController.view.isHidden = (index != 1)
    }

    @IBAction func onTapBack(_ sedner: Any) {
        self.pop(animationType: .horizontal)
    }
    
    @IBAction func onTapClient(_ sender: Any) {

        self.view.endEditing(true)
        self.changeSegment(index: 0)
    }
    
    @IBAction func onTapPartner(_ sender: Any) {

        self.view.endEditing(true)
        self.changeSegment(index: 1)
    }

}
