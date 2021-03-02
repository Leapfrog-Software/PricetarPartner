//
//  ChatMessageRightTableViewCell.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import UIKit

class ChatMessageRightTableViewCell: UITableViewCell {

    @IBOutlet private weak var datetimeLabel: UILabel!
    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var baseView: UIView!
    @IBOutlet private weak var messageLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.userImageView)
        self.userImageView.image = nil
    }
    
    func configure(chatData: ChatData) {
        
        self.datetimeLabel.text = chatData.getDisplayDatetime()
        ImageStorage.shared.fetch(url: UserData.imageUrl(userId: chatData.senderId), imageView: self.userImageView)
        self.messageLabel.text = chatData.message
    }
    
    func height(chatData: ChatData) -> CGFloat {
        
        self.configure(chatData: chatData)
        
        self.setNeedsLayout()
        self.layoutIfNeeded()
        
        return self.baseView.frame.origin.y + self.baseView.frame.size.height + 20
    }
}
