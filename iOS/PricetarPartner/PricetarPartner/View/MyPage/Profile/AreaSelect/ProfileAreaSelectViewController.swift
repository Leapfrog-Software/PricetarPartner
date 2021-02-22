//
//  ProfileAreaSelectViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class ProfileAreaSelectViewController: UIViewController {
    
    @IBOutlet private weak var contentsBottomConstraint: NSLayoutConstraint!
    @IBOutlet private weak var tableViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet private weak var tableView: UITableView!

    private var areas = ["北海道", "東北", "関東", "中部", "近畿", "中国", "四国", "九州"]
    private var selectedIndex = 0
    private var completion: ((String) -> ())?
    
    func set(defaultArea: String?, completion: ((String) -> ())?) {
        
        if let defaultArea = defaultArea, let index = self.areas.firstIndex(of: defaultArea) {
            self.selectedIndex = index
        }
        self.completion = completion
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.contentsBottomConstraint.constant = -356
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        self.contentsBottomConstraint.constant = 0
        self.tableViewBottomConstraint.constant = self.view.safeAreaInsets.bottom
        
        UIView.animate(withDuration: 0.2, animations: {
            self.view.layoutIfNeeded()
        })
    }
    
    private func close() {
        
        self.contentsBottomConstraint.constant = -356 - self.view.safeAreaInsets.bottom
        UIView.animate(withDuration: 0.2, animations: {
            self.view.layoutIfNeeded()
        }, completion: { _ in
            self.pop(animationType: .none)
        })
    }
    
    @IBAction func onTapUp(_ sender: Any) {
        
        if self.selectedIndex >= 1 {
            self.selectedIndex -= 1
            self.tableView.reloadData()
        }
    }
    
    @IBAction func onTapDown(_ sender: Any) {
        
        if self.selectedIndex <= self.areas.count - 2 {
            self.selectedIndex += 1
            self.tableView.reloadData()
        }
    }
    
    @IBAction func onTapOk(_ sender: Any) {
        
        self.completion?(self.areas[self.selectedIndex])
        self.close()
    }
    
    @IBAction func onTapBackground(_ sender: Any) {
        self.close()
    }

}

extension ProfileAreaSelectViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.areas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProfileAreaSelectTableViewCell", for: indexPath) as! ProfileAreaSelectTableViewCell
        cell.configure(isSelected: self.selectedIndex == indexPath.row, area: self.areas[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        self.selectedIndex = indexPath.row
        tableView.reloadData()
    }
}
