from selene.core.entity import Element
import logging


class BaseComponent:
    """Base component class matching Java BaseComponent"""

    def __init__(self, root_element: Element):
        self.logger = logging.getLogger(self.__class__.__name__)
        self._self = root_element

    @property
    def self(self) -> Element:
        """Get root element of the component"""
        return self._self
