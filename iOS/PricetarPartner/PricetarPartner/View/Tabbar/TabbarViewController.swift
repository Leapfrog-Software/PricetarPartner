//
//  TabbarViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class TabbarViewController: UIViewController {

    @IBOutlet private weak var containerView: UIView!
    
    private var homeViewController: HomeViewController!
    private var entryViewController: EntryViewController!
    private var searchViewController: SearchViewController!
    private var messageViewController: MessageViewController!
    private var myPageViewController: MyPageViewController!
    
    private let autoRequester = AutoRequester()
    
    func reload() {
        
        self.homeViewController.reload()
        self.entryViewController.reload()
        self.searchViewController.reload()
        self.messageViewController.reload()
        self.myPageViewController.reload()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let homeViewController = self.instantiate(storyboard: "Home", identifier: "HomeViewController") as! HomeViewController
        self.addViewController(viewController: homeViewController)
        self.homeViewController = homeViewController
        
        let entryViewController = self.instantiate(storyboard: "Entry", identifier: "EntryViewController") as! EntryViewController
        self.addViewController(viewController: entryViewController)
        self.entryViewController = entryViewController
        
        let searchViewController = self.instantiate(storyboard: "Search", identifier: "SearchViewController") as! SearchViewController
        self.addViewController(viewController: searchViewController)
        self.searchViewController = searchViewController
        
        let messageViewController = self.instantiate(storyboard: "Message", identifier: "MessageViewController") as! MessageViewController
        self.addViewController(viewController: messageViewController)
        self.messageViewController = messageViewController
        
        let myPageViewController = self.instantiate(storyboard: "MyPage", identifier: "MyPageViewController") as! MyPageViewController
        self.addViewController(viewController: myPageViewController)
        self.myPageViewController = myPageViewController
     
        self.changeTab(index: 0)
        
        self.startAutoRequester()
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
    
    private func changeTab(index: Int) {
        
        self.homeViewController.view.isHidden = (index != 0)
        self.entryViewController.view.isHidden = (index != 1)
        self.searchViewController.view.isHidden = (index != 2)
        self.messageViewController.view.isHidden = (index != 3)
        self.myPageViewController.view.isHidden = (index != 4)
    }
    
    func startAutoRequester() {
        
        self.autoRequester.start(didFetch: { [weak self] in
            self?.reload()
        })
    }
    
    func restartAutoRequester() {
        self.autoRequester.restart()
    }
    
    func stopAutoRequester() {
        self.autoRequester.stop()
    }
    
    @IBAction func onTapTab0(_ sender: Any) {
        self.changeTab(index: 0)
    }
    
    @IBAction func onTapTab1(_ sender: Any) {
        self.changeTab(index: 1)
    }
    
    @IBAction func onTapTab2(_ sender: Any) {
        self.changeTab(index: 2)
    }
    
    @IBAction func onTapTab3(_ sender: Any) {
        self.changeTab(index: 3)
    }
    
    @IBAction func onTapTab4(_ sender: Any) {
        self.changeTab(index: 4)
    }
}
