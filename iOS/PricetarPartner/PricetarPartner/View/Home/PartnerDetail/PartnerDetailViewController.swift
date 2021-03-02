//
//  PartnerDetailViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/26.
//

import UIKit

class PartnerDetailViewController: UIViewController {
    
    @IBOutlet private weak var headerNameLabel: UILabel!
    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var statusLabel: UILabel!
    @IBOutlet private weak var lastLoginDatetimeLabel: UILabel!
    @IBOutlet private weak var score0ImageView: UIImageView!
    @IBOutlet private weak var score1ImageView: UIImageView!
    @IBOutlet private weak var score2ImageView: UIImageView!
    @IBOutlet private weak var score3ImageView: UIImageView!
    @IBOutlet private weak var score4ImageView: UIImageView!
    @IBOutlet private weak var scoreLabel: UILabel!
    @IBOutlet private weak var areaLabel: UILabel!
    @IBOutlet private weak var profileTypeLabel: UILabel!
    @IBOutlet private weak var careerlabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var newPriceLabel: UILabel!
    @IBOutlet private weak var oldPriceLabel: UILabel!
    @IBOutlet private weak var optionTableView: UITableView!
    @IBOutlet private weak var optionTableViewHeightConstraint: NSLayoutConstraint!
    
    private var user: UserData!
    private var dummyOptionTableViewCell: PartnerDetailOptionTableViewCell?
    
    func set(user: UserData) {
        self.user = user
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.headerNameLabel.text = self.user.nickname
        
        ImageStorage.shared.fetch(url: UserData.imageUrl(userId: self.user.id), imageView: self.userImageView)
        self.userImageView.layer.cornerRadius = (UIScreen.main.bounds.size.width - 120) / 2
        
        self.nameLabel.text = self.user.nickname
        self.statusLabel.text = self.user.partnerStatus
        self.lastLoginDatetimeLabel.text = self.user.lastLoginString()
        
        // TODO レビュー
        
        self.areaLabel.text = self.user.area
        self.careerlabel.text = self.user.partnerCareer
        self.messageLabel.text = self.user.partnerMessage
        self.newPriceLabel.text = self.user.partnerNewPrice
        self.oldPriceLabel.text = self.user.partnerOldPrice
        
        self.dummyOptionTableViewCell = self.optionTableView.dequeueReusableCell(withIdentifier: "PartnerDetailOptionTableViewCell") as? PartnerDetailOptionTableViewCell
        self.dummyOptionTableViewCell?.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 1)
        
        var optionTableHeight = CGFloat(0)
        self.getOptions().forEach { _, message in
            optionTableHeight += (self.dummyOptionTableViewCell?.height(message: message) ?? 0)
        }
        self.optionTableViewHeightConstraint.constant = optionTableHeight
    }
    
    private func getOptions() -> [(price: String, message: String)] {

        var options = [(price: String, message: String)]()
        options.append((price: self.user.partnerDangerousPrice, message: self.user.partnerDangerousMessage))
        options.append((price: self.user.partnerBigPrice, message: self.user.partnerBigMessage))
        options.append((price: self.user.partnerInspectionPrice, message: self.user.partnerInspectionMessage))
        return options
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    @IBAction func onTapMessage(_ sender: Any) {
        
        let chat = self.instantiate(storyboard: "Message", identifier: "ChatViewController") as! ChatViewController
        chat.set(targetId: self.user.id)
        self.stack(viewController: chat, animationType: .horizontal)
    }
}

extension PartnerDetailViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.getOptions().count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let option = self.getOptions()[indexPath.row]
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "PartnerDetailOptionTableViewCell", for: indexPath) as! PartnerDetailOptionTableViewCell
        cell.configure(price: option.price, message: option.message)
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        let option = self.getOptions()[indexPath.row]
        return self.dummyOptionTableViewCell?.height(message: option.message) ?? 0
    }
}
